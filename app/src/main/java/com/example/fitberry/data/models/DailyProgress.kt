package com.example.fitberry.data.models

import java.text.SimpleDateFormat
import java.util.*

data class DailyProgress(
    val date: String, // Format: "yyyy-MM-dd"
    val userId: String = "",
    val caloriesConsumed: Int = 0,
    val caloriesGoal: Int = 2213,
    val proteinConsumed: Int = 0,
    val proteinGoal: Int = 90,
    val fatsConsumed: Int = 0,
    val fatsGoal: Int = 70,
    val carbsConsumed: Int = 0,
    val carbsGoal: Int = 110,
    val waterIntake: Int = 0, // in ml
    val waterGoal: Int = 2000,
    val workoutMinutes: Int = 0,
    val workoutGoal: Int = 30,
    val weight: Double? = null // Optional weight tracking for the day
) {
    // Helper to get date as Date object
    fun getDateAsDate(): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date) ?: Date()
    }

    // Helper to get day of week
    fun getDayOfWeek(): String {
        val dateObj = getDateAsDate()
        val calendar = Calendar.getInstance().apply { time = dateObj }
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }

    // Helper to get calorie percentage
    fun getCaloriePercentage(): Int {
        return if (caloriesGoal > 0) {
            (caloriesConsumed * 100 / caloriesGoal).coerceIn(0, 100)
        } else 0
    }

    // Helper to get protein percentage
    fun getProteinPercentage(): Int {
        return if (proteinGoal > 0) {
            (proteinConsumed * 100 / proteinGoal).coerceIn(0, 100)
        } else 0
    }

    // Helper to get fats percentage
    fun getFatsPercentage(): Int {
        return if (fatsGoal > 0) {
            (fatsConsumed * 100 / fatsGoal).coerceIn(0, 100)
        } else 0
    }

    // Helper to get carbs percentage
    fun getCarbsPercentage(): Int {
        return if (carbsGoal > 0) {
            (carbsConsumed * 100 / carbsGoal).coerceIn(0, 100)
        } else 0
    }

    // Helper to get remaining calories
    fun getRemainingCalories(): Int {
        return (caloriesGoal - caloriesConsumed).coerceAtLeast(0)
    }

    // Helper to check if goals are met
    fun isCalorieGoalMet(): Boolean = caloriesConsumed >= caloriesGoal
    fun isProteinGoalMet(): Boolean = proteinConsumed >= proteinGoal
    fun isFatsGoalMet(): Boolean = fatsConsumed >= fatsGoal
    fun isCarbsGoalMet(): Boolean = carbsConsumed >= carbsGoal
}