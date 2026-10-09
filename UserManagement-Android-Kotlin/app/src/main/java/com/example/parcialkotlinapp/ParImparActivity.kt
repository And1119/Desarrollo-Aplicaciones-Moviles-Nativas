package com.example.parcialkotlinapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ParImparActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_par_impar)

        // ACCIÓN DE LA FLECHA SUPERIOR DE REGRESO
        val btnVolver = findViewById<ImageView>(R.id.btnVolverParImpar)
        btnVolver?.setOnClickListener { finish() }

        val etNumero = findViewById<EditText>(R.id.etNumero)
        val btnValidar = findViewById<Button>(R.id.btnValidar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnValidar.setOnClickListener {
            val texto = etNumero.text.toString().trim()

            if (texto.isEmpty()) {
                etNumero.error = "Ingrese un número"
                return@setOnClickListener
            }

            val numero = texto.toIntOrNull()

            if (numero == null) {
                etNumero.error = "Ingrese un número entero válido"
                return@setOnClickListener
            }

            if (numero % 2 == 0) {
                tvResultado.text = "El número $numero es PAR"
            } else {
                tvResultado.text = "El número $numero es IMPAR"
            }
        }
    }
}