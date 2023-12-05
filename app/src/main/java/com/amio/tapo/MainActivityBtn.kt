package com.amio.tapo

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.concurrent.Executors

class MainActivityBtn {
    private val api: API = API()
    constructor(a: MainActivity) {
        val apiBtn = a.findViewById<Button>(R.id.apiBtn);
        val resultV = a.findViewById<TextView>(R.id.resultText);

        val donnees = Donnees()

        apiBtn.setOnClickListener {
            // Send GET to http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last in Executors

            Log.d("MainActivity", "Bouton API cliqué")

            val tsk = object : AsyncTask<Void, Void, String>() {
                override fun doInBackground(vararg params: Void?): String {
                    var result = ""
                    try {
                        result = api.getData()
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error: ${e.message}")
                    }
                    return result
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    if (result == "") {
                        Log.e("API", "Résultat vide")
                        Toast.makeText(a, "Je ne suis pas content de la réponse de l'API >:(", Toast.LENGTH_SHORT).show()
                        return
                    }
                    Log.d("API", "Résultat OK")
                    var diff = api.getDiff()
                    if (diff.size == 0) {
                        resultV.text = result
                    } else {
                        resultV.text = result + "\n\n" + diff.joinToString("\n")
                    }
                }
            }

            tsk.executeOnExecutor(Executors.newSingleThreadExecutor())
        }
    }
}