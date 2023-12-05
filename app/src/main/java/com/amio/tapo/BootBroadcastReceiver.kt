package com.amio.tapo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("MainActivity", "Boot completed")
        // Startup service
        val prefs = p0?.getSharedPreferences("com.amio.tapo", AppCompatActivity.MODE_PRIVATE)
        val startAtBoot = prefs?.getBoolean("startAtBoot", false)?:false
        if (startAtBoot) {
            p0?.startService(Intent(p0, MainService::class.java))
        }
    }
}