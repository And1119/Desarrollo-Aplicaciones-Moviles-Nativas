package com.example.parcialkotlinapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.models.CrearUsuarioRequest
import com.example.parcialkotlinapp.models.Usuario
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registro)

        // COMPONENTES
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreoRegistro)
        val etPasswordRegistro = findViewById<EditText>(R.id.etPasswordRegistro)

        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val etUniversidad = findViewById<EditText>(R.id.etUniversidad)
        val etSemestre = findViewById<EditText>(R.id.etSemestre)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val tvMensaje = findViewById<TextView>(R.id.tvMensajeRegistro)

        // BOTÓN REGISTRAR
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPasswordRegistro.text.toString().trim()
            val fechaNacimiento = etFechaNacimiento.text.toString().trim()
            val universidad = etUniversidad.text.toString().trim()
            val semestreTexto = etSemestre.text.toString().trim()

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

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()) {
                etCorreo.error = "Correo no válido"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                etPasswordRegistro.error = "Ingrese la contraseña"
                return@setOnClickListener
            }

            if (fechaNacimiento.isBlank()) {
                etFechaNacimiento.error = "Ingrese la fecha de nacimiento"
                return@setOnClickListener
            }

            if (universidad.isBlank()) {
                etUniversidad.error = "Ingrese la universidad"
                return@setOnClickListener
            }

            if (semestreTexto.isBlank()) {
                etSemestre.error = "Ingrese el semestre"
                return@setOnClickListener
            }

            val semestre = semestreTexto.toIntOrNull()

            if (semestre == null || semestre < 1 || semestre > 20) {
                etSemestre.error = "Semestre no válido"
                return@setOnClickListener
            }

            // OBJETO QUE ENVIAREMOS AL BACKEND
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

            // LLAMADA AL BACKEND
            ApiClient.usuarioApi
                .crearUsuario(nuevoUsuario)
                .enqueue(object : Callback<Usuario> {

                    override fun onResponse(
                        call: Call<Usuario>,
                        response: Response<Usuario>
                    ) {

                        if (response.isSuccessful) {

                            val usuarioCreado = response.body()

                            tvMensaje.text = "Usuario registrado correctamente"

                            Toast.makeText(
                                this@RegistroActivity,
                                "Usuario creado. ID: ${usuarioCreado?.id}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Limpiar formulario
                            etNombre.text.clear()
                            etApellido.text.clear()
                            etCorreo.text.clear()
                            etPasswordRegistro.text.clear()
                            etFechaNacimiento.text.clear()
                            etUniversidad.text.clear()
                            etSemestre.text.clear()

                        } else {

                            // LECTURA CONTROLADA DEL ERROR (400 / MENSAJE DEL BACKEND)
                            val errorBody = response.errorBody()?.string()

                            val mensajeError = try {
                                if (!errorBody.isNullOrEmpty()) {
                                    val jsonObject = JSONObject(errorBody)
                                    jsonObject.optString("mensaje", "Error del servidor: ${response.code()}")
                                } else {
                                    "Error del servidor: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                "Error del servidor: ${response.code()}"
                            }

                            tvMensaje.text = mensajeError
                        }
                    }

                    override fun onFailure(
                        call: Call<Usuario>,
                        t: Throwable
                    ) {

                        tvMensaje.text = "Error de conexión: ${t.message}"
                    }
                })
        }

        // VOLVER AL LOGIN
        btnVolver.setOnClickListener {
            finish()
        }
    }
}