package com.wacko1805.monetwebadapter

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Find the TextView by its ID
            val statusTextView: TextView = findViewById(R.id.statusTextView)

            // Set the text programmatically
            statusTextView.text = "Monet Web Adapter is running @ localhost:8192"


        // Start the service when the app launches
        startService(Intent(this, HttpServerService::class.java))
    }
}
