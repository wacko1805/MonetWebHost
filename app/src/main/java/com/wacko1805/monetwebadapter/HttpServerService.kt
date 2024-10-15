package com.wacko1805.monetwebadapter

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class HttpServerService : android.app.Service() {

    private lateinit var server: HttpServer

    override fun onCreate() {
        super.onCreate()
        server = HttpServer(applicationContext)
        Log.d("HttpServerService", "Service created and server initialized.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("HttpServerService", "onStartCommand called")

        createNotificationChannel()

        // Start the HTTP server in a separate thread
        Thread {
            Log.d("HttpServerService", "Starting HTTP server...")
            server.start()
            Log.d("HttpServerService", "HTTP server started.")
        }.start()

        // Create the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monet Web Adapter")
            .setContentText("Local Monet Web Adapter is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon resource
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true) // Makes the notification persistent
            .setAutoCancel(false) // Prevents the notification from being dismissed
            .setVisibility(NotificationCompat.VISIBILITY_SECRET) // Keeps it hidden
            .setCategory(NotificationCompat.CATEGORY_SERVICE) // Marks it as a service notification
            .build()

        // Start the service as a foreground service with the notification
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            } else {
                startForeground(1, notification)
            }
            Log.d("HttpServerService", "startForeground() called")
        } catch (e: Exception) {
            Log.e("HttpServerService", "Failed to startForeground", e)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HttpServerService", "Service destroyed")
        server.stop()
    }

    // Create notification channel for Android 8.0 and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monet Web Adapter",
                NotificationManager.IMPORTANCE_LOW // Use low importance so it stays in the notification bar without disturbing
            ).apply {
                description = "Notification channel for the HTTP server service"
            }

            // Create the notification channel
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Log.d("HttpServerService", "Notification channel created.")
        }
    }

    companion object {
        const val CHANNEL_ID = "http_server_channel"
    }
}
