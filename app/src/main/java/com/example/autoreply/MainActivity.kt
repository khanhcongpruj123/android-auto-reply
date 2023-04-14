package com.example.autoreply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import org.idev.autoreply.services.ForegroundNotificationService
import org.idev.autoreply.utils.PermissionUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.launchNotificationAccessSettings(this, 3105)

        val editText = findViewById<EditText>(R.id.edt_reply)

        val btnSend = findViewById<Button>(R.id.btn_send)

        btnSend.setOnClickListener {
            val intent = Intent(this, ForegroundNotificationService::class.java)
            intent.putExtra("reply_content", editText.text.toString())
            intent.putExtra("reply_to", "iamtest")
            startService(intent)
        }
    }
}