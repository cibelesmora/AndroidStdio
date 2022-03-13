package com.example.ficheros

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ficheros.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        //Comprobamos los permisos y los solicitamos si no están
        if ((ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    123
                    )
            }

        //Guardar recibe texto y lo almacena en la SD
        fun Guardar(texto: String){
            try {
                val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
                val miCarpeta = File(rutaSD, "datos")
                if (!miCarpeta.exists()) {
                    miCarpeta.mkdir()
                }
                val ficheroFisico = File(miCarpeta, "datos.txt")
                ficheroFisico.appendText("$texto. \n")
            }
            catch(e: Exception){
                Toast.makeText(
                    this,
                    "No ha sido posible guardar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Cargar devuelve lo almacenado en el fichero
        fun Cargar() :String{
            var texto = "" //Preparamos cadena vacía por si no conseguimos leer
            try {
                val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
                val miCarpeta = File(rutaSD, "datos")
                val ficheroFisico = File(miCarpeta, "datos.txt")
                val fichero = BufferedReader(
                    InputStreamReader(FileInputStream(ficheroFisico))
                )
                texto = fichero.use(BufferedReader::readText)
            }
            catch (e: Exception){
                //No hacemos nada
            }
            return texto
        }

        binding.btGuardar.setOnClickListener{
            Guardar(binding.etNuevoDato.text.toString())
            binding.tvContenido.text = Cargar()
        }

    }
}