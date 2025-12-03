package com.example.fitberry.data.models

import java.util.UUID

data class Meal(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val calories: Int,
    val protein: Int,
    val fats: Int,
    val carbs: Int,
    val mealType: MealType,
    val date: Long = System.currentTimeMillis(),
    val userId: String = ""
) {
    enum class MealType(val displayName: String) {
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        DINNER("Dinner"),
        SNACK("Snack"),
        OTHER("Other")
    }

    // Helper to get formatted date
    fun getFormattedDate(): String {
        return android.text.format.DateFormat.format("dd MMM yyyy, HH:mm", date).toString()
    }
}