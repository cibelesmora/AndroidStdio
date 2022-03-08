package com.example.almaceninterno

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.almaceninterno.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        //Aquí empieza el CASO 1 que emplea la memoria interna
        //CASO 1: Guardamos y leemos en el almacen interno que encontramos
        // en /data/data/<nombreApp>/files
        //Guardar recibe texto y lo almacena en la memoria interna
        fun guardar(texto: String) {
            try {
                //guardamos en el fichero datosInternos.txt el contenido de la variable texto
                val stream: FileOutputStream = openFileOutput("datosInternos.txt",
                    Context.MODE_APPEND)
                stream.use{ it.write(("$texto \n").toByteArray()) }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "No ha sido posible guardar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Cargar devuelve lo almacenado en el fichero
        fun cargar() :String{
            var texto = "" //Preparamos cadena vacía por si no conseguimos leer
            try {
                //Leesmos datosInternos.txt
                val fichero = BufferedReader(
                    InputStreamReader(
                    openFileInput("datosInternos.txt"))
                )
                texto = fichero.use(BufferedReader::readText)
            }
            catch (e: Exception){
                Toast.makeText(
                    this,
                    "No ha sido posible leer el fichero ${e.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return texto
        }
        //Aquí termina CASO 1



        /* //Aquí empieza CASO 2: USO DE CACHÉ
        //CASO 2: Guardamos y leemos de caché /data/data/<nombreApp>/cache
        //Guardar recibe texto y lo almacena en la memoria interna
        fun guardar(texto: String) {
            try {
                //Emplear el método createTempFile crea un fichero en caché con un sufijo en el nombre para que varíe cada vez
                //val fichero = File.createTempFile("ficheroCache", ".tmp", applicationContext.cacheDir)

                val fichero = File(applicationContext.cacheDir, "ficheroCache.tmp")
                fichero.appendText("$texto \n")
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "No ha sido posible guardar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Cargar devuelve lo almacenado en el fichero
        fun cargar() :String{
            var texto = "" //Preparamos cadena vacía por si no conseguimos leer
            try {

                val fichero = File(applicationContext.cacheDir, "ficheroCache.tmp")
                texto = FileInputStream(fichero).bufferedReader().use{
                    it.readText()
                }
            }
            catch (e: Exception){
                Toast.makeText(
                    this,
                    "No ha sido posible leer el fichero ${e.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return texto
        }

        //Aquí atendemos la llamada del botón Borrar Caché
        binding.btBorrarCache.setOnClickListener{
            val fichero = File(applicationContext.cacheDir, "ficheroCache.tmp")
            //Primero comprobamos que existe un fichero en la caché
            if (fichero.exists()){
                //Borramos el fichero de la caché
                fichero.delete()
                Toast.makeText(
                    this,
                    "La caché ha sido borrada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Toast.makeText(
                this,
                "No existe ningún fichero en la caché",
                Toast.LENGTH_SHORT
            ).show()
        }

        //Aquí eliminamos el contenido de la memoria caché justo antes de cerrar la aplicación
        override fun onStop() {
            applicationContext.cacheDir.deleteRecursively()
            super.onStop()
        }
        //Aquí ternina CASO 2 */


        //Aquí atendemos la llamada del botón guardar
        binding.btGuardar.setOnClickListener{
            //Guardar almacena el contenido que ponemos en la caja de texto en un fichero llamado datosInternos.txt
            guardar(binding.etNuevoDato.text.toString())

            //Cargar lee el contenido del fichero datosInternos.txt y lo muestra
            binding.tvContenido.text = cargar()
        }


    }
}