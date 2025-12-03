package com.example.fitberry.presentation.viewmodels.admindashboard

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.R
import com.example.fitberry.data.models.User
import com.example.fitberry.data.repository.FakeRepository

class UserFormActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var btnSave: TextView   // Save button is a TextView in XML

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_form)

        // Match XML IDs exactly
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        spinnerRole = findViewById(R.id.spinner_role)
        btnSave = findViewById(R.id.btn_save)

        // Spinner setup
        val roles = arrayOf("client", "nutritionniste", "admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        val action = intent.getStringExtra("ACTION")
        val userId = intent.getStringExtra("USER_ID")

        // Editing user → load data
        if (action == "EDIT" && userId != null) {
            val user = FakeRepository.adminUsers.find { it.id == userId }
            user?.let {
                etName.setText(it.name)
                etEmail.setText(it.email)
                spinnerRole.setSelection(roles.indexOf(it.role))
            }
        }

        btnSave.setOnClickListener {
            saveUser(action, userId)
        }
    }

    private fun saveUser(action: String?, userId: String?) {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val role = spinnerRole.selectedItem.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        when (action) {
            "ADD" -> {
                val newUser = User(
                    name = name,
                    email = email,
                    role = role
                )
                FakeRepository.addAdminUser(newUser)
                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
            }

            "EDIT" -> {
                val user = FakeRepository.adminUsers.find { it.id == userId }
                user?.let {
                    val updatedUser = it.copy(name = name, email = email, role = role)
                    FakeRepository.updateAdminUser(updatedUser)
                    Toast.makeText(this, "User updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        finish()
    }
}
