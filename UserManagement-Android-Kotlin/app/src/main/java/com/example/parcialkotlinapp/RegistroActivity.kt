package com.example.parcialkotlinapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.models.CrearUsuarioRequest
import com.example.parcialkotlinapp.models.Usuario
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // BOTÓN FLECHA DE REGRESAR EN EL LAYOUT PERSONALIZADO
        val btnVolver = findViewById<ImageView>(R.id.btnVolverRegistro)
        btnVolver?.setOnClickListener {
            finish()
        }

        // COMPONENTES
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreoRegistro)
        val etPasswordRegistro = findViewById<EditText>(R.id.etPasswordRegistro)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val tvEdadCalculada = findViewById<TextView>(R.id.tvEdadCalculada)
        val etUniversidad = findViewById<EditText>(R.id.etUniversidad)
        val spSemestre = findViewById<Spinner>(R.id.spSemestre)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvMensaje = findViewById<TextView>(R.id.tvMensajeRegistro)

        // POBLAR SPINNER DE SEMESTRE (1 A 10)
        val listaSemestres = (1..10).map { "Semestre $it" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaSemestres)
        spSemestre.adapter = adapter

        // MOSTRAR DATEPICKER Y CÁLCULO DE EDAD EXACTA
        fun mostrarDatePicker() {
            val calendario = Calendar.getInstance()
            val anioActual = calendario.get(Calendar.YEAR)
            val mesActual = calendario.get(Calendar.MONTH)
            val diaActual = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, yearSelected, monthSelected, daySelected ->
                    val mesFormateado = String.format(Locale.getDefault(), "%02d", monthSelected + 1)
                    val diaFormateado = String.format(Locale.getDefault(), "%02d", daySelected)
                    val fechaSeleccionada = "$yearSelected-$mesFormateado-$diaFormateado"

                    etFechaNacimiento.setText(fechaSeleccionada)
                    val edad = calcularEdad(yearSelected, monthSelected, daySelected)
                    tvEdadCalculada.text = "Edad: $edad años"
                },
                anioActual,
                mesActual,
                diaActual
            )
            datePickerDialog.show()
        }

        etFechaNacimiento.setOnClickListener { mostrarDatePicker() }
        etFechaNacimiento.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) mostrarDatePicker() }

        // BOTÓN REGISTRAR
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPasswordRegistro.text.toString().trim()
            val fechaNacimiento = etFechaNacimiento.text.toString().trim()
            val universidad = etUniversidad.text.toString().trim()

            // Obtener el semestre seleccionado (1 a 10)
            val semestre = spSemestre.selectedItemPosition + 1

            // VALIDACIONES
            if (nombre.isBlank()) {
                etNombre.error = "Ingrese el nombre"
                return@setOnClickListener
            }

            if (apellido.isBlank()) {
                etApellido.error = "Ingrese el apellido"
                return@setOnClickListener
            }

            if (correo.isBlank()) {
                etCorreo.error = "Ingrese el correo"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.error = "Correo no válido"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                etPasswordRegistro.error = "Ingrese la contraseña"
                return@setOnClickListener
            }

            if (fechaNacimiento.isBlank()) {
                etFechaNacimiento.error = "Seleccione la fecha de nacimiento"
                return@setOnClickListener
            }

            if (universidad.isBlank()) {
                etUniversidad.error = "Ingrese la universidad"
                return@setOnClickListener
            }

            val nuevoUsuario = CrearUsuarioRequest(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                password = password,
                fechaNacimiento = fechaNacimiento,
                universidad = universidad,
                semestre = semestre
            )

            tvMensaje.text = "Registrando usuario..."
            btnRegistrar.isEnabled = false

            ApiClient.usuarioApi.crearUsuario(nuevoUsuario).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    btnRegistrar.isEnabled = true

                    if (response.isSuccessful) {
                        tvMensaje.text = ""
                        AlertDialog.Builder(this@RegistroActivity)
                            .setTitle("Registro exitoso")
                            .setMessage("El usuario fue creado exitosamente.")
                            .setCancelable(false)
                            .setPositiveButton("ACEPTAR") { _, _ ->
                                val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val mensajeError = try {
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonObject = JSONObject(errorBody)
                                when {
                                    jsonObject.has("mensaje") -> jsonObject.getString("mensaje")
                                    jsonObject.has("message") -> jsonObject.getString("message")
                                    jsonObject.has("title") -> jsonObject.getString("title")
                                    else -> if (response.code() == 400) "Ya existe un usuario con ese correo." else "Error del servidor: ${response.code()}"
                                }
                            } else {
                                if (response.code() == 400) "Ya existe un usuario con ese correo." else "Error del servidor: ${response.code()}"
                            }
                        } catch (_: Exception) {
                            if (response.code() == 400) "Ya existe un usuario con ese correo." else "Error del servidor: ${response.code()}"
                        }

                        tvMensaje.text = mensajeError
                        AlertDialog.Builder(this@RegistroActivity)
                            .setTitle("Error de registro")
                            .setMessage(mensajeError)
                            .setPositiveButton("ACEPTAR", null)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    btnRegistrar.isEnabled = true
                    tvMensaje.text = "Error de conexión: ${t.message}"
                }
            })
        }
    }

    private fun calcularEdad(anioNac: Int, mesNac: Int, diaNac: Int): Int {
        val hoy = Calendar.getInstance()
        var edad = hoy.get(Calendar.YEAR) - anioNac
        if (hoy.get(Calendar.MONTH) < mesNac ||
            (hoy.get(Calendar.MONTH) == mesNac && hoy.get(Calendar.DAY_OF_MONTH) < diaNac)) {
            edad--
        }
        return edad
    }
}