package com.amio.tapo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask

class MainService : Service() {
    private var counter = 0
    private var timerTask = object: TimerTask() {
        override fun run() {
            counter++

            Log.d("MainService", "counter: $counter")
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("MainService", "Démarrage du service")

        // start thread for service
        Timer().schedule(timerTask, 0, 2000)

        // Démarrage en mode sticky
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainService", "Arrêt du service et du timer")
        timerTask.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}