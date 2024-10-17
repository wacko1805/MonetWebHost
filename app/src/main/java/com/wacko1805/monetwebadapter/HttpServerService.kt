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
import java.net.BindException

class HttpServerService : android.app.Service() {

    private lateinit var server: HttpServer
    private var isServerRunning = false

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

        // Start the HTTP server in a separate thread if it's not already running
        if (!isServerRunning) {
            Thread {
                try {
                    Log.d("HttpServerService", "Starting HTTP server...")
                    server.start()
                    isServerRunning = true
                    Log.d("HttpServerService", "HTTP server started.")
                } catch (e: BindException) {
                    Log.e("HttpServerService", "Port is already in use. Server not started.", e)
                } catch (e: Exception) {
                    Log.e("HttpServerService", "Error starting server", e)
                }
            }.start()
        } else {
            Log.d("HttpServerService", "HTTP server is already running.")
        }

        // Create the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monet Web Adapter")
            .setContentText("Local Monet Web Adapter is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon resource
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
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
        if (isServerRunning) {
            server.stop()
            isServerRunning = false
            Log.d("HttpServerService", "HTTP server stopped.")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monet Web Adapter",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification channel for the HTTP server service"
            }

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
