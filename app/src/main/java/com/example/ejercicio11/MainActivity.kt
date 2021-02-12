package com.example.ejercicio11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    //esta variable es la que voy a usar como request-code en el starActivityForResult
    val COMPRA_TARJETA=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //carga o infla el layout
        setContentView(R.layout.activity_main)

        //captura el botón de la vista y le pone un listener
        //Al pulsar el botón devuelve la lista de la compra
        //Si ha habido errores la lista llegara vacia ("")
        findViewById<Button>(R.id.main_bComprar).setOnClickListener {
            val compra: String = Comprar()
            //si la lista no esta vacia empieza la otra actividad
            if(compra!=""){
                //creamos el intent, donde le pasamos el contexto y el nombre de la nueva actividad (.kt)
                val miIntent=Intent(this,DatosTarjeta::class.java)
                //Añadimos al intent la lista de la compra para mostrarla en la nueva actividad
                miIntent.putExtra("compra",compra)
                //llamamos a la nueva actividad. Como esperamos un dato de vuelta usamos startActivityForResult
                startActivityForResult(miIntent,COMPRA_TARJETA)
            }

        }
    }

    //en esta función miramos a ver si hay algún error.
    //si no es así calculamos el total de la compra y la lista de la compra
    //esto último es lo que devuelve la función
    fun Comprar(): String {

        //capturamos todas las vistas que necesitamos
        val nombre=findViewById<EditText>(R.id.main_edNombre)
        val cocacola=findViewById<EditText>(R.id.main_edCocacola)
        val kasLimon=findViewById<EditText>(R.id.main_edKasLimon)
        val kasNaranja=findViewById<EditText>(R.id.main_edKasNaranja)
        val redBull=findViewById<EditText>(R.id.main_edRedBull)
        val vino=findViewById<EditText>(R.id.main_edVino)
        val cerveza=findViewById<EditText>(R.id.main_edCerveza)
        val edad=findViewById<CheckBox>(R.id.main_cbEdad)

        var errores=""
        var cantidad:Int
        var compra=""

        //miramos que el nombre y apellidos no esten vacios
        if (nombre.text.isEmpty()){
            errores=errores.plus("Tienes que insertar un nombre\n")
        }
        
        //miramos que si no han chequedo el mayor de edad no hayan cogido alcohol
        if (!edad.isChecked && (cerveza.text.isNotEmpty() || vino.text.isNotEmpty())){
            errores= errores.plus("No puedes comprar alcohol si no eres mayor de edad\n")
        }
        
        //si no ha habido errores empezamos a calcular la compra
        if (errores==""){
            var total=0.00
            
            //hay que asegurarnos de que no esten vacios antes de empezar a hacer calculos. Si no falla
            if (cocacola.text.isNotEmpty()) {
                cantidad=cocacola.text.toString().toInt()
                total = total + cantidad * 0.54
                compra += cantidad.toString() + " Cocacola =" + (cantidad * 0.54).toString() + "\n"
            }
            if (kasLimon.text.isNotEmpty()) {
                cantidad=kasLimon.text.toString().toInt()
                total = total + cantidad * 0.57
                compra += cantidad.toString() + " Kas Limón =" + (cantidad * 0.57).toString() + "\n"
            }
            if (kasNaranja.text.isNotEmpty()) {
                cantidad=kasNaranja.text.toString().toInt()
                total = total + cantidad * 0.57
                compra += cantidad.toString() + " Kas Naranja =" + (cantidad * 0.57).toString() + "\n"
            }
            if (redBull.text.toString().isNotEmpty()) {
                cantidad=redBull.text.toString().toInt()
                total = total + cantidad * 1.25
                compra += cantidad.toString() + " Red Bull =" + (cantidad * 1.25).toString() + "\n"
            }
            if (cerveza.text.toString().isNotEmpty()) {
                cantidad=cerveza.text.toString().toInt()
                total = total + cantidad * 0.62
                compra += cantidad.toString() + " Cerveza =" + (cantidad * 0.62).toString() + "\n"
            }
            if (vino.text.toString().isNotEmpty()) {
                cantidad=vino.text.toString().toInt()
                total = total + cantidad * 3.18
                compra += cantidad.toString() + " Botella de vino =" + (cantidad * 3.18).toString() + "\n"
            }
            
            //si el total sigue siendo 0 es que no ha seleccionado nada y sera un error
            //si el total es diferente de 0 insertamos en la lista el total
            if(total==0.00){
                errores+="No has seleccionado nada"
            }
            else{
                compra = compra + "Total =" + total
            }
        }
        
        //si ha habido errores los sacamos por pantalla
        if(errores!="") Toast.makeText(this,errores,Toast.LENGTH_SHORT).show()
        
        //devolvemos la lista de la compra
        return compra
    }

    //en esta función se recogen los resultados de las actividades que se han abierto usando un starActivityForResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        //hacemos el when para analizar cada una de las respuestas que pueda recibir
        //sera una por cada starActivityForResult que se haya hecho
        when(requestCode){
            COMPRA_TARJETA->{
                //si el resultCode es OK sacaremos por pantalla que la compra se ha realizado
                if(resultCode==RESULT_OK){
                    Toast.makeText(this,"Compra realizada con exito",Toast.LENGTH_SHORT).show()
                }
                //si no, sacaremos por pantalla los errores
                else{
                    //Recogemos los errores que vienen desde la otra actividad
                    val errores=data?.getStringExtra("errores") ?:""
                    Toast.makeText(this,"$errores No se ha podido realizar la compra",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}