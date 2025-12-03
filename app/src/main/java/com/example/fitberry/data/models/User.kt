package com.example.fitberry.data.models

import com.example.fitberry.R

data class User(
    val id: String = "",
    var name: String = "",
    var email: String = "",
    var role: String = "",
    val createdAt: String = "",
    val profileImage: String = ""
) {
    // Helper function to get role display color
    fun getRoleColor(): Int {
        return when (role.toLowerCase()) {
            "admin" -> R.color.orange_primary
            "editor" -> R.color.lime
            else -> R.color.purple_500
        }
    }
}