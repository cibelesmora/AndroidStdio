package com.example.sharedpreferences

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var nombre: String = ""
    var direccion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var binding: ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences(
            "datos", Context.MODE_PRIVATE
        )

        //Si no encuentra el dato, le damos un valor por defecto
        nombre = pref.getString("nombre","").toString()
        direccion = pref.getString("direccion","").toString()

        //Actualizamos la información del nombre y la dirección
        binding.tvDato1.text = nombre
        binding.tvDato2.text = direccion
        binding.etNombre.setText(nombre)
        binding.etDireccion.setText(direccion)

        binding.btGuardar.setOnClickListener{
            
            nombre = binding.etNombre.text.toString()
            direccion = binding.etDireccion.text.toString()

            //Actualizamos la información en la aplicación
            binding.tvDato1.text = nombre
            binding.etNombre.setText(nombre)
            binding.tvDato2.text = direccion
            binding.etDireccion.setText(direccion)

            val pref = applicationContext.getSharedPreferences(
                "datos", Context.MODE_PRIVATE
            )

            //Escribimos nombre y dirección
            val editor = pref.edit()
            editor.putString("nombre",nombre)
            editor.putString("direccion",direccion)
            editor.apply()
        }

    }
}