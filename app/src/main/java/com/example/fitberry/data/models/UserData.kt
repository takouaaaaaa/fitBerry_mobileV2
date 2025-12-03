package com.example.fitberry.data.models

import java.util.UUID

data class UserData(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "Mehrez Hrouz",
    var email: String = "mehrez@fitberry.com",
    var gender: String = "Male",
    var age: Int = 25,
    var weight: Double = 70.0,
    var height: Double = 175.0,
    var goal: String = "Maintain weight",
    var activityLevel: String = "Moderate",
    var dailyCalories: Int = 2213,
    var proteinGoal: Int = 90,
    var fatsGoal: Int = 70,
    var carbsGoal: Int = 110,
    val createdAt: Long = System.currentTimeMillis()
) {
    // Helper function to get BMI
    fun calculateBMI(): Double {
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }

    // Helper function to get BMI category
    fun getBMICategory(): String {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
    }
}