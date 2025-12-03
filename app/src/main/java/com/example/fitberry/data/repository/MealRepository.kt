package com.example.fitberry.data.repository

import com.example.fitberry.data.models.Meal
import java.text.SimpleDateFormat
import java.util.*

interface MealRepository {
    suspend fun addMeal(meal: Meal): Result<Unit>
    suspend fun getMealById(id: String): Meal?
    suspend fun updateMeal(meal: Meal): Result<Unit>
    suspend fun deleteMeal(id: String): Result<Unit>

    suspend fun getTodayMeals(): List<Meal>
    suspend fun getMealsByDate(dateMillis: Long): List<Meal>
    suspend fun getMealsByType(mealType: Meal.MealType): List<Meal>
    suspend fun getMealsByDateRange(startDate: Long, endDate: Long): List<Meal>

    suspend fun getTodayCalories(): Int
    suspend fun getTodayMacros(): Map<String, Int>
    suspend fun getWeeklyMealSummary(): Map<String, Any>
}

class FakeMealRepository : MealRepository {

    override suspend fun addMeal(meal: Meal): Result<Unit> {
        return try {
            FakeRepository.addMeal(meal)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMealById(id: String): Meal? {
        return FakeRepository.meals.find { it.id == id }
    }

    override suspend fun updateMeal(meal: Meal): Result<Unit> {
        return try {
            val index = FakeRepository.meals.indexOfFirst { it.id == meal.id }
            if (index != -1) {
                // First remove old meal to update progress
                val oldMeal = FakeRepository.meals[index]
                FakeRepository.updateProgressAfterMealDeletion(oldMeal)

                // Update the meal
                FakeRepository.meals[index] = meal

                // Add new meal to update progress
                FakeRepository.updateTodayProgress(meal)

                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Meal not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMeal(id: String): Result<Unit> {
        return try {
            val success = FakeRepository.deleteMeal(id)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Meal not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodayMeals(): List<Meal> {
        return FakeRepository.getTodayMeals()
    }

    override suspend fun getMealsByDate(dateMillis: Long): List<Meal> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        return FakeRepository.meals.filter { it.date in startOfDay..endOfDay }
            .sortedByDescending { it.date }
    }

    override suspend fun getMealsByType(mealType: Meal.MealType): List<Meal> {
        return FakeRepository.meals.filter { it.mealType == mealType }
            .sortedByDescending { it.date }
    }

    override suspend fun getMealsByDateRange(startDate: Long, endDate: Long): List<Meal> {
        return FakeRepository.meals.filter { it.date in startDate..endDate }
            .sortedByDescending { it.date }
    }

    override suspend fun getTodayCalories(): Int {
        return FakeRepository.getTotalCaloriesConsumedToday()
    }

    override suspend fun getTodayMacros(): Map<String, Int> {
        val todayMeals = FakeRepository.getTodayMeals()
        return mapOf(
            "calories" to todayMeals.sumOf { it.calories },
            "protein" to todayMeals.sumOf { it.protein },
            "fats" to todayMeals.sumOf { it.fats },
            "carbs" to todayMeals.sumOf { it.carbs }
        )
    }

    override suspend fun getWeeklyMealSummary(): Map<String, Any> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -6)
        val startDate = calendar.timeInMillis
        val endDate = System.currentTimeMillis()

        val weeklyMeals = getMealsByDateRange(startDate, endDate)

        val mealsByDay = mutableMapOf<String, List<Meal>>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        weeklyMeals.forEach { meal ->
            val dateStr = dateFormat.format(Date(meal.date))
            val mealsForDay = mealsByDay.getOrDefault(dateStr, emptyList()) + meal
            mealsByDay[dateStr] = mealsForDay
        }

        val averageCalories = if (weeklyMeals.isNotEmpty()) {
            weeklyMeals.sumOf { it.calories } / 7
        } else 0

        // Find most common meal type
        val mealTypeCounts = weeklyMeals.groupBy { it.mealType }
        val mostCommonMealType = mealTypeCounts.maxByOrNull { it.value.size }?.key?.displayName ?: "None"

        return mapOf(
            "totalMeals" to weeklyMeals.size,
            "averageCaloriesPerDay" to averageCalories,
            "mealsByDay" to mealsByDay,
            "mostCommonMealType" to mostCommonMealType
        )
    }
}