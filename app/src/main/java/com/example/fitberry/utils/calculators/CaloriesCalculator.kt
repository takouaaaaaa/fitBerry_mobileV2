package com.example.fitberry.utils.calculators

object CalorieCalculator {

    fun calculateBMR(gender: String, weight: Double, height: Double, age: Int): Double {
        return when (gender.lowercase()) {
            "male" -> 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
            "female" -> 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
            else -> 500 + (10 * weight) + (6.25 * height) - (5 * age)
        }
    }

    fun calculateTDEE(bmr: Double, activityLevel: String): Double {
        val multiplier = when (activityLevel) {
            "Sedentary" -> 1.2
            "Light" -> 1.375
            "Moderate" -> 1.55
            "Active" -> 1.725
            "Very Active" -> 1.9
            else -> 1.55
        }
        return bmr * multiplier
    }

    fun calculateGoalCalories(tdee: Double, goal: String): Double {
        return when (goal.lowercase()) {
            "lose weight" -> tdee - 500
            "gain weight" -> tdee + 500
            else -> tdee
        }
    }

    fun calculateMacros(calories: Double, weight: Double): Triple<Int, Int, Int> {
        // Protein: 1.6-2.2g per kg of body weight
        val proteinGrams = (weight * 1.8).toInt()

        // Fats: 20-35% of total calories
        val fatCalories = calories * 0.25
        val fatGrams = (fatCalories / 9).toInt() // 9 calories per gram of fat

        // Carbs: remaining calories
        val proteinCalories = proteinGrams * 4 // 4 calories per gram of protein
        val carbCalories = calories - (proteinCalories + fatCalories)
        val carbGrams = (carbCalories / 4).toInt() // 4 calories per gram of carbs

        return Triple(proteinGrams, fatGrams, carbGrams)
    }

    fun calculateMacroPercentages(calories: Double, protein: Int, fats: Int, carbs: Int): Triple<Int, Int, Int> {
        val proteinCalories = protein * 4
        val fatsCalories = fats * 9
        val carbsCalories = carbs * 4

        val proteinPercentage = (proteinCalories * 100 / calories).toInt()
        val fatsPercentage = (fatsCalories * 100 / calories).toInt()
        val carbsPercentage = (carbsCalories * 100 / calories).toInt()

        return Triple(proteinPercentage, fatsPercentage, carbsPercentage)
    }

    fun getActivityLevelMultiplier(activityLevel: String): Double {
        return when (activityLevel) {
            "Sedentary" -> 1.2
            "Light" -> 1.375
            "Moderate" -> 1.55
            "Active" -> 1.725
            "Very Active" -> 1.9
            else -> 1.55
        }
    }

    fun getWeightChangePerWeek(currentWeight: Double, dailyCalories: Int, goal: String): Double {
        val tdee = calculateTDEE(
            calculateBMR("male", currentWeight, 175.0, 30), // Using default values
            "Moderate"
        )

        val calorieDifference = dailyCalories - tdee
        // 7700 calories ≈ 1 kg of body weight
        return (calorieDifference * 7 / 7700.0)
    }
}