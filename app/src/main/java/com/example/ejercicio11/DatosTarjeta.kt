package com.example.ejercicio11

import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class DatosTarjeta : AppCompatActivity() {

    //Esto lo vamos a necesitar para poder usar los metodos de fechas
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //carga o infla el layout
        setContentView(R.layout.activity_datos_tarjeta)

        //coge los datos que vienen de la actividad que la ha abierto
        val compra:String=intent.extras?.getString("compra") ?:""

        //Si la compra no ha llegado o ha llegado vacia informamos con un toast y terminamos la actividad
        //si no capturamos el textview de la vista y le insertamos la compra
        if (compra==""){
            Toast.makeText(this, "Ha habido un error. Vuelva a hacer la compra", Toast.LENGTH_SHORT).show()
            finish()
        }
        else{
            findViewById<TextView>(R.id.datos_tvCompra).text=compra
        }


        //Capturamos el botón de la vista y le ponemos un listener
        //Cuando pulsamos el botón llamamos a la función Validar que nos devolvera
        //un string vacio "" si no hay errores y uno lleno si los hay
        //Si no hay errores se devuelve un OK
        //Si hay errores se devuelve un CANCELED y el string con los errores
        findViewById<Button>(R.id.datos_bComprar).setOnClickListener{
            val errores=Validar()
            if(errores==""){
                setResult(Activity.RESULT_OK)
            }
            else{
                val miIntencion= Intent().putExtra("errores",errores)
                setResult(Activity.RESULT_CANCELED, miIntencion)
            }
            finish()
        }
    }

    //Esto lo vamos a necesitar para poder usar los metodos de fechas
    @RequiresApi(Build.VERSION_CODES.N)
    fun Validar():String{

        //string donde voy a guardar los errores que puedan surgir
        var errores=""

        //capturar los elementos de la vista que me hacen falta para validar
        val titular=findViewById<EditText>(R.id.datos_etNombre)
        val numTarjeta=findViewById<EditText>(R.id.datos_etNumTarjeta)
        val fecha=findViewById<EditText>(R.id.datos_etFecha)
        val cvv=findViewById<EditText>(R.id.datos_etCVV)

        //Vamos a usar los regex para validar el número de la tarjeta y el cvv
        //le pido que meta tantas cifras como hay entre las llaves
        //y que cada cifra solo puede coger uno de los valores que esta en el intervalo
        val regexNumTarjeta = Regex("""[0-9]{16}""")
        val regexCVV = Regex("""[0-9]{3}""")

        //si el titular esta vacio insertamos el error en el string errores
        if (titular.text.isEmpty()) errores+="No has insertado un titular\n"

        //para usar el regex usamos el metodo del string .matches al que le pasamos el Regex
        //si no coincide con el patron insertamos el error en el string errores
        if(!numTarjeta.text.toString().matches(regexNumTarjeta)) errores+="Inserta un número de tarjeta valido\n"

        //Si la fecha es menor a la de hoy insertamos el error en el string errores
        if(fecha.text.toString()<= Calendar.getInstance().toString()) errores+="Esa tarjeta esta caducada\n"

        //si el cvv o coincide con el patron insertamos el error en el string errores
        if(!cvv.text.toString().matches(regexCVV)) errores+="Inserta un número CVV valido\n"

        //devolvemos los errores. Si no ha habido estara vacio ""
        return errores
    }
}