package com.example.ficheros

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.ficheros.databinding.ActivityMainBinding
import java.io.*
import java.lang.ProcessBuilder.Redirect.to
import java.nio.charset.StandardCharsets
import java.util.Objects.toString

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

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
        fun Guardar(texto: String, fichero: String){
            try {
                val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
                val encryptedFile = EncryptedFile.Builder(
                    File(rutaSD, fichero),
                    applicationContext,
                    mainKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build()

                val contenido = texto.toByteArray(StandardCharsets.UTF_8)
                encryptedFile.openFileOutput().apply {
                    write(contenido)
                    flush()
                    close()
                }

            }
            catch(e: Exception){
                Toast.makeText(
                    this,
                    "No ha sido posible guardar " + e.toString() ,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Cargar devuelve lo almacenado en el fichero
        fun Cargar( fichero:String ) :String{
            var texto = "" //Preparamos cadena vacía por si no conseguimos leer
            try {
                val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
                val miCarpeta = File(rutaSD, fichero)
                val encryptedFile = EncryptedFile.Builder(
                    miCarpeta,
                    applicationContext,
                    mainKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build()
                val inputStream = encryptedFile.openFileInput()
                val byteArrayOutputStream = ByteArrayOutputStream()
                var nextByte: Int = inputStream.read()
                while (nextByte != -1) {
                    byteArrayOutputStream.write(nextByte)
                    nextByte = inputStream.read()
                }
                var plaintext: ByteArray = byteArrayOutputStream.toByteArray()
                texto = plaintext.toString(charset("UTF8"))
            }
            catch (e: Exception){
                Toast.makeText(
                    this,
                    "No ha sido posible cargar el fichero " + e.toString() ,
                    Toast.LENGTH_SHORT
                ).show()
            }
            return texto
        }

        binding.btGuardar.setOnClickListener{
            Guardar( binding.etNuevoDato.text.toString(), binding.etFichero.text.toString())
            binding.tvContenido.text = Cargar(binding.etFichero.text.toString())
        }

    }
}