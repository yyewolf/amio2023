package com.amio.tapo

import android.Manifest
import android.app.AppComponentFactory
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Timer
import java.util.TimerTask

class MainService : Service() {
    private var counter = 0
    private var api = API()
    private var timerTask = object: TimerTask() {
        override fun run() {
            counter++
            Log.d("MainService", "TimerTask")
            api.get()
            val diff = api.getDiff()
            // Send push notification if diff is not empty and if it is between 18h and 23h
            val isBetween18hAnd23h = (18..23).contains(java.time.LocalDateTime.now().hour)
            val isWeekend = (java.time.LocalDateTime.now().dayOfWeek.toString() == "SATURDAY" || java.time.LocalDateTime.now().dayOfWeek.toString() == "SUNDAY")
            val isBetween23hAnd6h = (23..6).contains(java.time.LocalDateTime.now().hour)
            if (diff.isNotEmpty() && !isWeekend && isBetween18hAnd23h) {
                for (d in diff) {
                    val notificationChannel = NotificationChannel(
                        "com.amio.tapo",
                        "Tapo",
                        NotificationManager.IMPORTANCE_HIGH,
                    )

                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    var builder = NotificationCompat.Builder(this@MainService, "com.amio.tapo")
                        .setSmallIcon(com.google.android.material.R.drawable.navigation_empty_icon)
                        .setChannelId(notificationChannel.id)
                        .setContentTitle("Pas de changement d'état " + counter.toString())
                        .setPriority(NotificationCompat.PRIORITY_MAX)


                    notificationManager.createNotificationChannel(notificationChannel)
                    notificationManager.notify(counter, builder.build())
                }
            }

            if (diff.isNotEmpty() && ((!isWeekend && isBetween23hAnd6h) || (isWeekend && isBetween18hAnd23h))) {
                val m = Mail()
                // Get mail from preferences "email"
                val prefs = getSharedPreferences("com.amio.tapo", AppCompatActivity.MODE_PRIVATE)
                val email = prefs.getString("email", "")
                val username = prefs.getString("username", "User")
                if (email != null) {
                    m.send(email, "TAPO - Changes detected", "Hello ${username}, here's what happened:\n\n"+diff.joinToString("\n"))
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("MainService", "Démarrage du service")

        // start thread for service
        Timer().schedule(timerTask, 0, 30000)

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