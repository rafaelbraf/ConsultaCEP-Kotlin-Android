package com.example.consultacep


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.Error

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_consultarCEP.setOnClickListener {
            val cep = editText_CEP.text.toString()
            getData(cep)
        }

    }

    fun getData(cep: String) {

        val client = OkHttpClient()

        try {

            val url = "http://viacep.com.br/ws/$cep/json/"

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    createThread(response)
                }
            })

        } catch (err: Error) {
            println("Erro na requisição ${err.localizedMessage}")
        }

    }

    fun createThread(response: Response) {
        val thread = Thread(
            Runnable {
                runOnUiThread {
                    val jsonObject = JSONTokener(response.body?.string()).nextValue() as JSONObject
                    textView_logradouro.text = jsonObject.getString("logradouro")
                    textView_bairro.text = jsonObject.getString("bairro")
                    textView_cidade.text = jsonObject.getString("localidade")
                    textView_estado.text = jsonObject.getString("uf")
                    textView_codigo.text = jsonObject.getString("ddd")
                }
            }
        )
        thread.start()
    }

}