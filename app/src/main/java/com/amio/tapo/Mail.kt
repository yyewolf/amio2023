package com.amio.tapo

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.Objects
import java.util.concurrent.Executors
import kotlin.math.log

class Mail {
    private val server = "aspmx.l.google.com"
    private val port = 25
    private val from = "amio@telecomnancy.net"

    fun send(to: String, subject: String, body: String) {

        Log.d("Mail", "Sending mail to ${to}")
        val tsk = object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {
                try {
                    val client = Socket(server, port)
                    val writer = BufferedWriter(OutputStreamWriter(client.getOutputStream(), "UTF-8"))
                    writer.write("HELO amio.com\r\n")

                    writer.flush()
                    var line = client.getInputStream().bufferedReader().readLine()
                    Log.d("Mail", line)

                    writer.write("MAIL FROM: <$from>\r\n")
                    writer.flush()
                    line = client.getInputStream().bufferedReader().readLine()
                    Log.d("Mail", line)
                    writer.write("RCPT TO: <$to>\r\n")
                    writer.flush()
                    line = client.getInputStream().bufferedReader().readLine()
                    Log.d("Mail", line)
                    writer.write("DATA\r\n")
                    writer.flush()
                    line = client.getInputStream().bufferedReader().readLine()
                    Log.d("Mail", line)
                    writer.write("Subject: $subject\r\n")
                    writer.write("From: $from\r\n")
                    writer.write("To: $to\r\n")
                    writer.write("Message-ID: <${System.currentTimeMillis()}@amio.com>\r\n")
                    writer.write("\r\n")
                    writer.write("$body\r\n")
                    writer.write(".\r\n")
                    writer.write("QUIT\r\n")

                    writer.flush()
                    val lines = client.getInputStream().bufferedReader().readLines()
                    Log.d("Mail", lines.joinToString("\n"))

                    client.close()
                } catch (e: Exception) {
                    Log.e("Mail", "Error: ${e.message}")
                }
                return ""
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d("Mail", "Mail sent")
            }
        }

        tsk.executeOnExecutor(Executors.newSingleThreadExecutor())
    }
}