package com.example.fitberry.presentation.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.presentation.activities.onboarding.AgePickerActivity
import com.example.fitberry.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegisterConfirm = findViewById<Button>(R.id.btnRegisterConfirm)

        btnRegisterConfirm.setOnClickListener {
            val intent = Intent(this, AgePickerActivity::class.java)
            startActivity(intent)
        }
    }
}