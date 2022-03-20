package com.example.encryptedsharedpref

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.encryptedsharedpref.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Declaramos las variables que vamos a emplear
    var nombre: String = ""
    var direccion: String = ""

    //Establecemos los parámetros de la encriptación: AES 256
    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var binding: ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        //Creamos el objeto EncryptedSharedPreferences para almacenar en el fichero datosEncriptados
        val pref = EncryptedSharedPreferences.create(
            "datosEncriptados",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        //Con getString recuperamos datos del fichero
        //Si el fichero o el dato no existe, le damos un valor por defecto
        nombre = pref.getString("nombre","").toString()
        direccion = pref.getString("direccion","").toString()

        //Mostramos los datos
        binding.tvDato1.text = nombre
        binding.tvDato2.text = direccion

        //Incluimos los datos en la caja de texto si ya estaban almacenados
        binding.etNombre.setText(nombre)
        binding.etDireccion.setText(direccion)

        //Acciones cuando se pulsa el botón
        binding.btGuardar.setOnClickListener{
            //Recuperamos el texto de las cajas
            nombre = binding.etNombre.text.toString()
            direccion = binding.etDireccion.text.toString()

            //Actualizamos el valor de los textView
            binding.tvDato1.text = nombre
            binding.tvDato2.text = direccion

            //Creamos el objeto pref que permite editar en el fichero datosEncriptados
            val pref = EncryptedSharedPreferences.create(
                "datosEncriptados",
                masterKeyAlias,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val editor = pref.edit()
            //Escribimos los campos y aplicamos cambios
            editor.putString("nombre",nombre)
            editor.putString("direccion",direccion)
            editor.apply()
        }

    }
}