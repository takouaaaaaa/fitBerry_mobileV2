package com.example.fitberry.data.repository

import com.example.fitberry.data.models.DailyProgress
import java.text.SimpleDateFormat
import java.util.*

interface ProgressRepository {
    suspend fun getTodayProgress(): DailyProgress
    suspend fun updateProgress(progress: DailyProgress): Result<Unit>
    suspend fun resetTodayProgress(): Result<Unit>

    suspend fun getProgressByDate(date: String): DailyProgress?
    suspend fun getWeeklyProgress(): List<DailyProgress>
    suspend fun getMonthlyProgress(): List<DailyProgress>
    suspend fun getProgressHistory(startDate: String, endDate: String): List<DailyProgress>

    suspend fun getDailyStats(): Map<String, Any>
    suspend fun getWeeklyStats(): Map<String, Any>
    suspend fun getProgressTrend(): Map<String, Any>
    suspend fun getGoalAchievementRate(): Map<String, Int>
}

class FakeProgressRepository : ProgressRepository {

    override suspend fun getTodayProgress(): DailyProgress {
        return FakeRepository.getTodayProgress()
    }

    override suspend fun updateProgress(progress: DailyProgress): Result<Unit> {
        return try {
            val existingIndex = FakeRepository.dailyProgress.indexOfFirst { it.date == progress.date }
            if (existingIndex != -1) {
                FakeRepository.dailyProgress[existingIndex] = progress
            } else {
                FakeRepository.dailyProgress.add(progress)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetTodayProgress(): Result<Unit> {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(Date())

            val existingIndex = FakeRepository.dailyProgress.indexOfFirst { it.date == todayStr }
            if (existingIndex != -1) {
                FakeRepository.dailyProgress[existingIndex] = FakeRepository.dailyProgress[existingIndex].copy(
                    caloriesConsumed = 0,
                    proteinConsumed = 0,
                    fatsConsumed = 0,
                    carbsConsumed = 0
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProgressByDate(date: String): DailyProgress? {
        return FakeRepository.dailyProgress.find { it.date == date }
    }

    override suspend fun getWeeklyProgress(): List<DailyProgress> {
        return FakeRepository.getWeeklyProgress()
    }

    override suspend fun getMonthlyProgress(): List<DailyProgress> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        calendar.add(Calendar.DAY_OF_MONTH, -29)
        val startDateStr = dateFormat.format(calendar.time)
        val endDateStr = dateFormat.format(Date())

        return FakeRepository.dailyProgress
            .filter { it.date >= startDateStr && it.date <= endDateStr }
            .sortedBy { it.date }
    }

    override suspend fun getProgressHistory(startDate: String, endDate: String): List<DailyProgress> {
        return FakeRepository.dailyProgress
            .filter { it.date >= startDate && it.date <= endDate }
            .sortedBy { it.date }
    }

    override suspend fun getDailyStats(): Map<String, Any> {
        val progress = getTodayProgress()
        val user = FakeRepository.currentUser

        return mapOf(
            "calories" to mapOf(
                "consumed" to progress.caloriesConsumed,
                "goal" to progress.caloriesGoal,
                "remaining" to (progress.caloriesGoal - progress.caloriesConsumed).coerceAtLeast(0),
                "percentage" to progress.getCaloriePercentage()
            ),
            "protein" to mapOf(
                "consumed" to progress.proteinConsumed,
                "goal" to progress.proteinGoal,
                "percentage" to progress.getProteinPercentage()
            ),
            "fats" to mapOf(
                "consumed" to progress.fatsConsumed,
                "goal" to progress.fatsGoal,
                "percentage" to progress.getFatsPercentage()
            ),
            "carbs" to mapOf(
                "consumed" to progress.carbsConsumed,
                "goal" to progress.carbsGoal,
                "percentage" to progress.getCarbsPercentage()
            ),
            "isGoalMet" to progress.isCalorieGoalMet(),
            "userName" to (user?.name ?: "User")
        )
    }

    override suspend fun getWeeklyStats(): Map<String, Any> {
        val weeklyProgress = getWeeklyProgress()

        val totalCalories = weeklyProgress.sumOf { it.caloriesConsumed }
        val averageCalories = if (weeklyProgress.isNotEmpty()) totalCalories / weeklyProgress.size else 0
        val goalMetDays = weeklyProgress.count { it.isCalorieGoalMet() }

        val bestDay = weeklyProgress.maxByOrNull { it.caloriesConsumed }
        val worstDay = weeklyProgress.minByOrNull { it.caloriesConsumed }

        return mapOf(
            "totalCalories" to totalCalories,
            "averageDailyCalories" to averageCalories,
            "goalMetDays" to goalMetDays,
            "goalAchievementRate" to if (weeklyProgress.isNotEmpty()) (goalMetDays * 100 / weeklyProgress.size) else 0,
            "bestDay" to mapOf(
                "date" to bestDay?.date,
                "calories" to bestDay?.caloriesConsumed
            ),
            "worstDay" to mapOf(
                "date" to worstDay?.date,
                "calories" to worstDay?.caloriesConsumed
            ),
            "trend" to if (weeklyProgress.size >= 2) {
                val lastTwoDays = weeklyProgress.takeLast(2)
                lastTwoDays[1].caloriesConsumed - lastTwoDays[0].caloriesConsumed
            } else 0
        )
    }

    override suspend fun getProgressTrend(): Map<String, Any> {
        val weeklyProgress = getWeeklyProgress()

        val calorieTrend = weeklyProgress.map { it.caloriesConsumed }
        val proteinTrend = weeklyProgress.map { it.proteinConsumed }
        val fatsTrend = weeklyProgress.map { it.fatsConsumed }
        val carbsTrend = weeklyProgress.map { it.carbsConsumed }

        val dates = weeklyProgress.map { it.getDayOfWeek() }

        return mapOf(
            "dates" to dates,
            "calories" to calorieTrend,
            "protein" to proteinTrend,
            "fats" to fatsTrend,
            "carbs" to carbsTrend,
            "calorieAverage" to if (calorieTrend.isNotEmpty()) calorieTrend.average().toInt() else 0,
            "isImproving" to if (calorieTrend.size >= 2) {
                calorieTrend.last() > calorieTrend.first()
            } else false
        )
    }

    override suspend fun getGoalAchievementRate(): Map<String, Int> {
        val weeklyProgress = getWeeklyProgress()

        val calorieDays = weeklyProgress.count { it.isCalorieGoalMet() }
        val proteinDays = weeklyProgress.count { it.isProteinGoalMet() }
        val fatsDays = weeklyProgress.count { it.isFatsGoalMet() }
        val carbsDays = weeklyProgress.count { it.isCarbsGoalMet() }

        val totalDays = weeklyProgress.size

        return mapOf(
            "calories" to if (totalDays > 0) (calorieDays * 100 / totalDays) else 0,
            "protein" to if (totalDays > 0) (proteinDays * 100 / totalDays) else 0,
            "fats" to if (totalDays > 0) (fatsDays * 100 / totalDays) else 0,
            "carbs" to if (totalDays > 0) (carbsDays * 100 / totalDays) else 0
        )
    }
}