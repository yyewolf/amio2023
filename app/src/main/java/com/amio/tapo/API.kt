package com.amio.tapo

import android.os.Looper
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class API {
    val donnees = Donnees()

    fun getData(): String {
        val url = URL("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last")
        val urlConnection = url.openConnection() as HttpURLConnection
        var result = ""
        try {
            // Check HTTP response code
            if (urlConnection.responseCode != 200) {
                throw Exception("Failed to connect")
            }
            val input = urlConnection.inputStream.bufferedReader().readText()
            result = input
        } finally {
            urlConnection.disconnect()
        }

        if (result == "") {
            return ""
        }

        val obj = object: JSONObject(result) {};
        val arr = obj.getJSONArray("data");
        var text = ""
        for (i in 0 until arr.length()) {
            val data = arr.getJSONObject(i)
            val timestamp = data.getLong("timestamp")
            val date = java.util.Date(timestamp * 1000)
            val value = data.getDouble("value")
            val donneeLabel = DonneesLabel(
                timestamp = timestamp,
                value = data.getDouble("value")
            )
            donnees.addData(
                salle="salle",
                mote=data.getString("mote"),
                label=data.getString("label"),
                donnees=donneeLabel,
            )
            Log.d("API", value.toString())

            val state = if (value < 250) "OFF" else "ON"

            text += "${data.getString("mote")}: ${data.getString("value")} (${state})\n\n"
        }

        return text
    }

    // Function to get which lamp changed from ON to OFF or OFF to ON
    fun getDiff(): List<String> {
        var out: MutableList<String> = mutableListOf()
        for (salle in donnees.salles()) {
            for (mote in donnees.motes(salle)) {
                for (label in donnees.labels(salle, mote)) {
                    val datas = donnees.donnees(salle, mote, label)
                    if (datas.size < 2) {
                        continue
                    }
                    val i = datas.size - 2
                    if (datas[i].value < 250 && datas[i + 1].value > 250) {
                        // Lamp turned ON
                        out.add("salle $salle, mote $mote, label $label went ON")
                    } else if (datas[i].value > 250 && datas[i + 1].value < 250) {
                        // Lamp turned OFF
                        out.add("salle $salle, mote $mote, label $label went OFF")
                    }
                }
            }
        }
        return out
    }
    fun get() {
        val url = URL("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last")
        val urlConnection = url.openConnection() as HttpURLConnection
        var result = ""
        try {
            // Check HTTP response code
            if (urlConnection.responseCode != 200) {
                throw Exception("Failed to connect")
            }
            val input = urlConnection.inputStream.bufferedReader().readText()
            result = input
        } finally {
            urlConnection.disconnect()
        }

        val obj = object: JSONObject(result) {};
        val arr = obj.getJSONArray("data");
        for (i in 0 until arr.length()) {
            val data = arr.getJSONObject(i)
            val timestamp = data.getLong("timestamp")
            val date = java.util.Date(timestamp * 1000)
            val value = data.getDouble("value")
            val donneeLabel = DonneesLabel(
                timestamp = timestamp,
                value = data.getDouble("value")
            )
            donnees.addData(
                salle="salle",
                mote=data.getString("mote"),
                label=data.getString("label"),
                donnees=donneeLabel,
            )
        }
    }
}