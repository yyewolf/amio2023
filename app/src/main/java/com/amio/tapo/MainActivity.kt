package com.amio.tapo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    public var prefs: android.content.SharedPreferences? = null
    var api = API()

    val vibrator: Vibrator by lazy {
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Content is main_activity_view.xml
        setContentView(R.layout.main_activity_view)

        prefs = getSharedPreferences("com.amio.tapo", AppCompatActivity.MODE_PRIVATE)

        Log.d("MainActivity", "Création de l'activité")

        // startService(Intent(this, MainService::class.java))

        // Set default positions for switches
        toggleSwitch(findViewById(R.id.switch1), true)
        toggleCheckBox(findViewById(R.id.checkBox), true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun openSettings(v: android.view.View) {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

    fun toggleSwitch(v: android.view.View) {
        toggleSwitch(v as Switch, false)
    }

    fun toggleCheckBox(v: android.view.View) {
        toggleCheckBox(v as android.widget.CheckBox, false)
    }

    var annoyingCounter = 0
    private fun  vibrate(){
        annoyingCounter += 1
        val pattern = longArrayOf(0, 200, 0, 200)
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        Log.d("t",vibrator.hasVibrator().toString())
        if (annoyingCounter == 5) {
            Toast.makeText(this, "Arrête de cliquer sur le bouton!", Toast.LENGTH_SHORT).show()
        }
        if (annoyingCounter == 7) {
            Toast.makeText(this, "Je t'aurais prévenu!!", Toast.LENGTH_SHORT).show()
        }
        if (annoyingCounter == 10) {
            // Infinite vibration
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            Toast.makeText(this, "Je t'avais prévenu...", Toast.LENGTH_SHORT).show()
        }
    }

    fun vibrate(v: android.view.View) {
        // Small animation to make it clear
        v.isEnabled = false
        Log.d("MainActivity", "Vibrate")

        v.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100).withEndAction {
            v.animate().scaleXBy(-0.1f).scaleYBy(-0.1f).setDuration(100).start()
        }.start()

        vibrate()

        // Wait for animation end :
        v.postDelayed({
            v.isEnabled = true
        }, 200)
    }

    fun callAPI(v: android.view.View) {
        // Disable button to avoid confusion
        v.isEnabled = false

        // Small animation to make it clear
        v.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100).withEndAction {
            v.animate().scaleXBy(-0.1f).scaleYBy(-0.1f).setDuration(100).start()
        }.start()

        val resultV = findViewById<TextView>(R.id.resultText)

        Log.d("MainActivity", "Bouton API cliqué")

        // Execute what's next in another thread
        Executors.newSingleThreadExecutor().execute {
            Looper.prepare()
            Toast.makeText(this, "Appel de l'API", Toast.LENGTH_SHORT).show()
            var result = ""
            try {
                result = api.getData()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e}")
            }

            if (result == "") {
                Log.e("API", "Résultat vide")
                Toast.makeText(this, "Je ne suis pas content de la réponse de l'API >:(", Toast.LENGTH_SHORT).show()
                runOnUiThread() {
                    resultV.text = "Erreur"
                    v.isEnabled = true
                }
                return@execute
            }
            Log.d("API", "Résultat OK")
            Toast.makeText(this, "Réponse reçu!", Toast.LENGTH_SHORT).show()
            var diff = api.getDiff()
            if (diff.isEmpty()) {
                runOnUiThread {
                    resultV.text = result
                    v.isEnabled = true
                }
            } else {
                result = "${result}\n\n${diff.joinToString("\n")}"
                runOnUiThread {
                    resultV.text = result
                    v.isEnabled = true
                }
            }
        }
    }

    private fun toggleCheckBox(v: android.widget.CheckBox, init: Boolean = false) {
        if (init) {
            v.isChecked = prefs!!.getBoolean("startAtBoot", false)
        }

        if (v.isChecked) {
            Log.d("MainActivity", "CheckBox checked")
            prefs!!.edit().putBoolean("startAtBoot", true).apply()
        } else {
            Log.d("MainActivity", "CheckBox unchecked")
            prefs!!.edit().putBoolean("startAtBoot", false).apply()
        }
    }
    private fun toggleSwitch(v: Switch, init: Boolean = false) {
        val textView2 = findViewById<TextView>(R.id.textView2)

        if (init) {
            v.isChecked = prefs!!.getBoolean("startAtBoot", false)
            v.invalidate()
            Log.d("MainActivity", "Toggle switch to ${v.isChecked}")
        }

        if (v.isChecked) {
            textView2.text = "En cours"
            Log.d("MainActivity", "Creation du service")
            // Start mainService
            stopService(Intent(this, MainService::class.java))
            startService(Intent(this, MainService::class.java))
        } else {
            textView2.text = "Arrêt"
            Log.d("MainActivity", "Arret du service")
            stopService(Intent(this, MainService::class.java))
        }
    }

    fun sendTestMail(v: android.view.View) {
        v.isEnabled = false
        // Small animation to make it clear
        v.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100).withEndAction {
            v.animate().scaleXBy(-0.1f).scaleYBy(-0.1f).setDuration(100).start()
        }.start()

        val m = Mail()
        // Get mail from preferences "email"
        // preferences were created in Settings by setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val prefs = getSharedPreferences("com.amio.tapo_preferences", AppCompatActivity.MODE_PRIVATE)
        // Print all files under shared_prefs
        val email = prefs.getString("email", "")
        val username = prefs.getString("username", "User")
        Log.d("MainActivity", "Send test mail to ${email}")
        if (email != null) {
            m.send(email, "TAPO - Test email", "Hello ${username}, this is a test email from TAPO.")
        }
        // Wait for animation end :
        v.postDelayed({
            v.isEnabled = true
        }, 200)
    }
}
