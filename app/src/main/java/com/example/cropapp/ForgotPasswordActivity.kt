package com.example.cropapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnSendResetLink = findViewById<Button>(R.id.btnSendResetLink)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        btnSendResetLink.setOnClickListener {
            Toast.makeText(this, "Password reset link sent", Toast.LENGTH_SHORT).show()
        }

        tvBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
