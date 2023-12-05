package com.amio.tapo

import android.content.Intent
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivitySwitch1 {
    constructor(a: MainActivity) {
        val switch1 = a.findViewById<Switch>(R.id.switch1)
        val textView2 = a.findViewById<TextView>(R.id.textView2)

        switch1.isChecked = a.prefs!!.getBoolean("startAtBoot", false)

        if (switch1.isChecked) {
            Log.d("MainActivity", "Switch1 pre-checked")
            textView2.text = "En cours"
        } else {
            Log.d("MainActivity", "Switch1 pre-unchecked")
            textView2.text = "Arrêt"
        }

        Log.d("MainActivity", "Création du listener Switch1")

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textView2.text = "En cours"
                Log.d("MainActivity", "Creation du service")
                // Start mainService
                a.startService(Intent(a, MainService::class.java))
            } else {
                textView2.text = "Arrêt"
                Log.d("MainActivity", "Arret du service")
                a.stopService(Intent(a, MainService::class.java))
            }
        }
    }
}