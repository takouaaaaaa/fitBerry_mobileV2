package com.example.fitberry.presentation.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.R
import com.example.fitberry.presentation.viewmodels.admindashboard.AdminDashboardActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLoginConfirm)

        btnLogin.setOnClickListener {
            // Go to Admin Dashboard
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}