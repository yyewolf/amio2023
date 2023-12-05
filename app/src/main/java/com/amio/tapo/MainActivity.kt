package com.amio.tapo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class MainActivity : ComponentActivity() {

    public var prefs: android.content.SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("com.amio.tapo", AppCompatActivity.MODE_PRIVATE)

        // Content is main_activity_view.xml
        setContentView(R.layout.main_activity_view)

        Log.d("MainActivity", "Création de l'activité")

        // startService(Intent(this, MainService::class.java))

        // Create MainActivitySwitch1 to create listener for switch1
        val mainActivitySwitch1 = MainActivitySwitch1(this)
        val mainActivityCheckBox = MainActivityCheckBox(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
