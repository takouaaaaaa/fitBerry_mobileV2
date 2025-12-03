package com.example.fitberry.presentation.activities.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.R
import com.example.fitberry.presentation.activities.auth.LoginActivity
import com.example.fitberry.presentation.activities.auth.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // On associe cette activité à son layout
        setContentView(R.layout.activity_welcome)

        // Récupération des boutons
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // -------------------------
        // Bouton Sign In → LoginActivity
        // -------------------------
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // ferme WelcomeActivity pour ne pas revenir dessus
        }

        // -------------------------
        // Bouton Sign Up → RegisterActivity
        // -------------------------
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish() // ferme WelcomeActivity pour ne pas revenir dessus
        }
    }
}