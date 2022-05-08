package com.example.helloworld

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SayHiBtn.setOnClickListener {sayHello()}
    }

    private fun sayHello(){
        if(CajaNombre.text.toString().isEmpty()){
            Toast.makeText(this, "Indica tu nombre", Toast.LENGTH_SHORT).show()
            }else{
            Toast.makeText(this, "Hola ${CajaNombre.text}!!",
                Toast.LENGTH_SHORT).show()
            }
        }
}