package com.amio.tapo

import android.content.Intent
import android.util.Log
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class MainActivityCheckBox {
    constructor(a: MainActivity) {
        val checkbox = a.findViewById<CheckBox>(R.id.checkBox)
        checkbox.isChecked = a.prefs!!.getBoolean("startAtBoot", false)

        if (checkbox.isChecked) {
            Log.d("MainActivity", "CheckBox pre-checked")
            a.startService(Intent(a, MainService::class.java))
        }

        Log.d("MainActivity", "Création du listener CheckBox")

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d("MainActivity", "CheckBox checked")
                a.prefs!!.edit().putBoolean("startAtBoot", true).apply()
            } else {
                Log.d("MainActivity", "CheckBox unchecked")
                a.prefs!!.edit().putBoolean("startAtBoot", false).apply()
            }
        }
    }
}