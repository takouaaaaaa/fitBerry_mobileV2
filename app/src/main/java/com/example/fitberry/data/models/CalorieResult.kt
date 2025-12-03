package com.example.fitberry.data.models

data class CalorieResult(
    val bmr: Double,                     // Basal Metabolic Rate
    val tdee: Double,                    // Total Daily Energy Expenditure
    val targetCalories: Double,          // Calories for goal
    val proteinGoal: Int,                // Protein in grams
    val fatsGoal: Int,                   // Fats in grams
    val carbsGoal: Int,                  // Carbs in grams
    val maintenanceCalories: Double = tdee, // Maintenance calories
    val weightLossCalories: Double = tdee - 500, // For 0.5kg/week loss
    val weightGainCalories: Double = tdee + 500  // For 0.5kg/week gain
) {
    // Helper to get formatted result
    fun getFormattedResult(): String {
        return """
            BMR: ${"%.0f".format(bmr)} kcal/day
            TDEE: ${"%.0f".format(tdee)} kcal/day
            Target: ${"%.0f".format(targetCalories)} kcal/day
            Protein: ${proteinGoal}g/day
            Fats: ${fatsGoal}g/day
            Carbs: ${carbsGoal}g/day
        """.trimIndent()
    }

    // Helper to get macros in calories
    fun getProteinCalories(): Int = proteinGoal * 4
    fun getFatsCalories(): Int = fatsGoal * 9
    fun getCarbsCalories(): Int = carbsGoal * 4

    // Helper to get macro percentages
    fun getProteinPercentage(): Int = (getProteinCalories() * 100 / targetCalories.toInt())
    fun getFatsPercentage(): Int = (getFatsCalories() * 100 / targetCalories.toInt())
    fun getCarbsPercentage(): Int = (getCarbsCalories() * 100 / targetCalories.toInt())
}