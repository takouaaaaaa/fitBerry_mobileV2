package com.example.fitberry.data.repository

import com.example.fitberry.data.models.UserData
import com.example.fitberry.data.models.CalorieResult

interface UserRepository {
    suspend fun saveUser(user: UserData): Result<Unit>
    suspend fun getCurrentUser(): UserData?
    suspend fun updateUserProfile(user: UserData): Result<Unit>
    suspend fun deleteUser(): Result<Unit>

    suspend fun calculateCalorieNeeds(user: UserData): CalorieResult
    suspend fun recalculateUserGoals(user: UserData): Result<CalorieResult>

    suspend fun getUserStats(): Map<String, Any>
    suspend fun updateUserGoals(goals: Map<String, Any>): Result<Unit>
}

class FakeUserRepository : UserRepository {

    override suspend fun saveUser(user: UserData): Result<Unit> {
        return try {
            FakeRepository.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): UserData? {
        return FakeRepository.currentUser
    }

    override suspend fun updateUserProfile(user: UserData): Result<Unit> {
        return try {
            FakeRepository.updateUserProfile(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(): Result<Unit> {
        return try {
            FakeRepository.currentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun calculateCalorieNeeds(user: UserData): CalorieResult {
        val bmr = com.example.fitberry.utils.calculators.CalorieCalculator.calculateBMR(
            user.gender, user.weight, user.height, user.age
        )
        val tdee = com.example.fitberry.utils.calculators.CalorieCalculator.calculateTDEE(bmr, user.activityLevel)
        val targetCalories = com.example.fitberry.utils.calculators.CalorieCalculator.calculateGoalCalories(tdee, user.goal)
        val (protein, fats, carbs) = com.example.fitberry.utils.calculators.CalorieCalculator.calculateMacros(targetCalories, user.weight)

        return CalorieResult(
            bmr = bmr,
            tdee = tdee,
            targetCalories = targetCalories,
            proteinGoal = protein,
            fatsGoal = fats,
            carbsGoal = carbs
        )
    }

    override suspend fun recalculateUserGoals(user: UserData): Result<CalorieResult> {
        return try {
            val result = FakeRepository.recalculateUserCalories(user)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserStats(): Map<String, Any> {
        val user = FakeRepository.currentUser
        val progress = FakeRepository.getTodayProgress()

        return mapOf(
            "dailyCalories" to (user?.dailyCalories ?: 2213),
            "caloriesConsumed" to progress.caloriesConsumed,
            "caloriesRemaining" to FakeRepository.getRemainingCaloriesToday(),
            "proteinGoal" to (user?.proteinGoal ?: 90),
            "proteinConsumed" to progress.proteinConsumed,
            "fatsGoal" to (user?.fatsGoal ?: 70),
            "fatsConsumed" to progress.fatsConsumed,
            "carbsGoal" to (user?.carbsGoal ?: 110),
            "carbsConsumed" to progress.carbsConsumed,
            "progressPercentage" to FakeRepository.getDailyProgressPercentage()
        )
    }

    override suspend fun updateUserGoals(goals: Map<String, Any>): Result<Unit> {
        return try {
            val user = FakeRepository.currentUser ?: return Result.failure(
                IllegalStateException("No user found")
            )

            val updatedUser = user.copy(
                dailyCalories = goals["dailyCalories"] as? Int ?: user.dailyCalories,
                proteinGoal = goals["proteinGoal"] as? Int ?: user.proteinGoal,
                fatsGoal = goals["fatsGoal"] as? Int ?: user.fatsGoal,
                carbsGoal = goals["carbsGoal"] as? Int ?: user.carbsGoal
            )

            FakeRepository.updateUserProfile(updatedUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}