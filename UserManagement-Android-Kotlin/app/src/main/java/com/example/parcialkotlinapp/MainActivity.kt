package com.example.parcialkotlinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Saludo personalizado por hora
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val nombreExtra = intent.getStringExtra("NOMBRE_USUARIO")
        val nombreUsuario = if (!nombreExtra.isNullOrEmpty()) nombreExtra else "Usuario"

        val horaActual = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val saludo = when (horaActual) {
            in 5..11 -> "Buenos días, $nombreUsuario"
            in 12..17 -> "Buenas tardes, $nombreUsuario"
            else -> "Buenas noches, $nombreUsuario"
        }

        tvBienvenida.text = saludo

        // Botón Par o Impar
        val btnParImpar = findViewById<Button>(R.id.btnParImpar)
        btnParImpar.setOnClickListener {
            val intent = Intent(this, ParImparActivity::class.java)
            startActivity(intent)
        }

        // Ícono superior de cerrar sesión (Paso 10 del diagrama)
        val btnCerrarSesionTop = findViewById<ImageView>(R.id.btnCerrarSesionTop)
        btnCerrarSesionTop?.setOnClickListener {
            mostrarDialogoCierreSesion()
        }
    }

    private fun mostrarDialogoCierreSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Está seguro de que desea cerrar sesión?")
            .setPositiveButton("SALIR") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("CANCELAR") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}