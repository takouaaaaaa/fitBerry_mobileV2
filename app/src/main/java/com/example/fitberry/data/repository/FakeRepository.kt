package com.example.fitberry.data.repository

import com.example.fitberry.data.models.Article
import com.example.fitberry.data.models.User
import com.example.fitberry.data.models.UserData
import com.example.fitberry.data.models.Meal
import com.example.fitberry.data.models.DailyProgress
import com.example.fitberry.data.models.CalorieResult
import com.example.fitberry.utils.calculators.CalorieCalculator
import java.text.SimpleDateFormat
import java.util.*

object FakeRepository {
    // ============ ADMIN DASHBOARD DATA ============
    val adminUsers = mutableListOf<User>()
    val articles = mutableListOf<Article>()

    // ============ FITBERRY APP DATA ============
    var currentUser: UserData? = null
    val meals = mutableListOf<Meal>()
    val dailyProgress = mutableListOf<DailyProgress>()
    var userCalorieResult: CalorieResult? = null

    init {
        seedAdminData()
        // Don't seed FitBerry data automatically - it will be seeded when user completes onboarding
    }

    private fun seedAdminData() {
        // Seed admin users
        adminUsers.add(User(
            name = "Alice Duval",
            email = "alice@example.com",
            role = "client"
        ))
        adminUsers.add(User(
            name = "Dr. Karim",
            email = "karim@nutri.com",
            role = "nutritionniste"
        ))
        adminUsers.add(User(
            name = "Salah Ben",
            email = "salah@example.com",
            role = "client"
        ))

        // Seed articles
        articles.add(Article(
            title = "Healthy Eating",
            content = "A comprehensive guide to healthy eating habits."
        ))
        articles.add(Article(
            title = "Protein Myths",
            content = "Debunking common myths about protein intake."
        ))
        articles.add(Article(
            title = "Weight Loss Tips",
            content = "Effective strategies for sustainable weight loss."
        ))
    }

    private fun seedFitBerryData(user: UserData) {
        // Set current user
        currentUser = user
        userCalorieResult = calculateUserCalorieResult(user)

        // Clear existing data
        meals.clear()
        dailyProgress.clear()

        seedSampleMeals()
        seedWeeklyProgress(user)
    }

    // ============ CALORIE CALCULATION ============

    private fun calculateUserCalorieResult(user: UserData): CalorieResult {
        val bmr = CalorieCalculator.calculateBMR(user.gender, user.weight, user.height, user.age)
        val tdee = CalorieCalculator.calculateTDEE(bmr, user.activityLevel)
        val targetCalories = CalorieCalculator.calculateGoalCalories(tdee, user.goal)
        val (protein, fats, carbs) = CalorieCalculator.calculateMacros(targetCalories, user.weight)

        return CalorieResult(
            bmr = bmr,
            tdee = tdee,
            targetCalories = targetCalories,
            proteinGoal = protein,
            fatsGoal = fats,
            carbsGoal = carbs
        )
    }

    fun recalculateUserCalories(user: UserData): CalorieResult {
        val result = calculateUserCalorieResult(user)
        userCalorieResult = result
        user.dailyCalories = result.targetCalories.toInt()
        user.proteinGoal = result.proteinGoal
        user.fatsGoal = result.fatsGoal
        user.carbsGoal = result.carbsGoal
        return result
    }

    // ============ MEAL METHODS ============

    private fun seedSampleMeals() {
        val today = Calendar.getInstance().timeInMillis

        meals.add(Meal(
            name = "Oatmeal with Berries",
            calories = 450,
            protein = 15,
            fats = 8,
            carbs = 75,
            mealType = Meal.MealType.BREAKFAST,
            date = today
        ))

        meals.add(Meal(
            name = "Grilled Chicken Salad",
            calories = 650,
            protein = 45,
            fats = 25,
            carbs = 30,
            mealType = Meal.MealType.LUNCH,
            date = today
        ))

        meals.add(Meal(
            name = "Salmon with Vegetables",
            calories = 621,
            protein = 18,
            fats = 12,
            carbs = 80,
            mealType = Meal.MealType.DINNER,
            date = today
        ))
    }

    fun addMeal(meal: Meal) {
        meals.add(meal)
        updateTodayProgress(meal)
    }

    fun getTodayMeals(): List<Meal> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        return meals.filter { it.date in startOfDay..endOfDay }
            .sortedByDescending { it.date }
    }

    fun deleteMeal(mealId: String): Boolean {
        val meal = meals.find { it.id == mealId }
        return if (meal != null) {
            meals.remove(meal)
            updateProgressAfterMealDeletion(meal)
            true
        } else {
            false
        }
    }

    // ============ PROGRESS METHODS ============

    private fun seedWeeklyProgress(user: UserData) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Get user's actual goals
        val calorieGoal = user.dailyCalories
        val proteinGoal = user.proteinGoal
        val fatsGoal = user.fatsGoal
        val carbsGoal = user.carbsGoal

        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_MONTH, -i)
            val dateStr = dateFormat.format(calendar.time)

            // Calculate percentages based on user's goals
            val caloriesConsumed = when (i) {
                0 -> (calorieGoal * 0.78).toInt() // 78% of goal
                1 -> (calorieGoal * 0.86).toInt()
                2 -> (calorieGoal * 0.99).toInt()
                3 -> (calorieGoal * 0.95).toInt()
                4 -> (calorieGoal * 0.81).toInt()
                5 -> (calorieGoal * 0.90).toInt()
                else -> (calorieGoal * 0.95).toInt()
            }

            dailyProgress.add(DailyProgress(
                date = dateStr,
                caloriesConsumed = caloriesConsumed,
                caloriesGoal = calorieGoal,
                proteinConsumed = (proteinGoal * 0.78).toInt().coerceAtMost(proteinGoal),
                proteinGoal = proteinGoal,
                fatsConsumed = (fatsGoal * 0.64).toInt().coerceAtMost(fatsGoal),
                fatsGoal = fatsGoal,
                carbsConsumed = (carbsGoal * 0.86).toInt().coerceAtMost(carbsGoal),
                carbsGoal = carbsGoal
            ))
        }
    }

    fun updateTodayProgress(meal: Meal) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(Date())

        var progress = dailyProgress.find { it.date == todayStr }

        if (progress == null) {
            progress = DailyProgress(
                date = todayStr,
                caloriesGoal = currentUser?.dailyCalories ?: 2213,
                proteinGoal = currentUser?.proteinGoal ?: 90,
                fatsGoal = currentUser?.fatsGoal ?: 70,
                carbsGoal = currentUser?.carbsGoal ?: 110,
                caloriesConsumed = 0,
                proteinConsumed = 0,
                fatsConsumed = 0,
                carbsConsumed = 0
            )
            dailyProgress.add(progress)
        }

        val updatedProgress = progress.copy(
            caloriesConsumed = progress.caloriesConsumed + meal.calories,
            proteinConsumed = progress.proteinConsumed + meal.protein,
            fatsConsumed = progress.fatsConsumed + meal.fats,
            carbsConsumed = progress.carbsConsumed + meal.carbs
        )

        dailyProgress.remove(progress)
        dailyProgress.add(updatedProgress)
    }

    fun updateProgressAfterMealDeletion(meal: Meal) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val mealDateStr = dateFormat.format(Date(meal.date))

        val progress = dailyProgress.find { it.date == mealDateStr }
        progress?.let {
            val updatedProgress = it.copy(
                caloriesConsumed = it.caloriesConsumed - meal.calories,
                proteinConsumed = it.proteinConsumed - meal.protein,
                fatsConsumed = it.fatsConsumed - meal.fats,
                carbsConsumed = it.carbsConsumed - meal.carbs
            )
            dailyProgress.remove(it)
            dailyProgress.add(updatedProgress)
        }
    }

    fun getTodayProgress(): DailyProgress {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(Date())

        return dailyProgress.find { it.date == todayStr } ?: DailyProgress(
            date = todayStr,
            caloriesGoal = currentUser?.dailyCalories ?: 2213,
            proteinGoal = currentUser?.proteinGoal ?: 90,
            fatsGoal = currentUser?.fatsGoal ?: 70,
            carbsGoal = currentUser?.carbsGoal ?: 110,
            caloriesConsumed = 0,
            proteinConsumed = 0,
            fatsConsumed = 0,
            carbsConsumed = 0
        )
    }

    fun getWeeklyProgress(): List<DailyProgress> {
        return dailyProgress.sortedBy { it.date }.takeLast(7)
    }

    // ============ USER METHODS ============

    fun saveUser(user: UserData) {
        currentUser = user
        userCalorieResult = calculateUserCalorieResult(user)
        // Seed data with the new user
        seedFitBerryData(user)
    }

    fun updateUserProfile(updatedUser: UserData) {
        currentUser = updatedUser
        userCalorieResult = calculateUserCalorieResult(updatedUser)

        // Update existing daily progress with new goals
        dailyProgress.forEach { progress ->
            val updatedProgress = progress.copy(
                caloriesGoal = updatedUser.dailyCalories,
                proteinGoal = updatedUser.proteinGoal,
                fatsGoal = updatedUser.fatsGoal,
                carbsGoal = updatedUser.carbsGoal
            )
            dailyProgress.remove(progress)
            dailyProgress.add(updatedProgress)
        }
    }

    // ============ ADMIN USER METHODS ============

    fun addAdminUser(user: User) {
        adminUsers.add(user)
    }

    fun getAdminUserById(id: String): User? {
        return adminUsers.find { it.id == id }
    }

    fun updateAdminUser(updatedUser: User) {
        val index = adminUsers.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            adminUsers[index] = updatedUser
        }
    }

    fun deleteAdminUser(id: String) {
        adminUsers.removeAll { it.id == id }
    }

    fun getAllAdminUsers(): List<User> {
        return adminUsers.sortedBy { it.name }
    }

    // ============ ARTICLE METHODS ============

    fun addArticle(article: Article) {
        articles.add(article)
    }

    fun getArticleById(id: String): Article? {
        return articles.find { it.id == id }
    }

    fun updateArticle(updatedArticle: Article) {
        val index = articles.indexOfFirst { it.id == updatedArticle.id }
        if (index != -1) {
            articles[index] = updatedArticle
        }
    }

    fun deleteArticle(id: String) {
        articles.removeAll { it.id == id }
    }

    fun getAllArticles(): List<Article> {
        return articles.sortedByDescending { it.publishDate }
    }

    // ============ UTILITY METHODS ============

    fun getTotalCaloriesConsumedToday(): Int {
        return getTodayMeals().sumOf { it.calories }
    }

    fun getRemainingCaloriesToday(): Int {
        val goal = currentUser?.dailyCalories ?: 2213
        val consumed = getTotalCaloriesConsumedToday()
        return (goal - consumed).coerceAtLeast(0)
    }

    fun getDailyProgressPercentage(): Int {
        val progress = getTodayProgress()
        val goal = currentUser?.dailyCalories ?: progress.caloriesGoal
        return if (goal > 0) {
            (progress.caloriesConsumed * 100 / goal).coerceIn(0, 100)
        } else 0
    }

    // Helper function to check if user exists
    fun hasUser(): Boolean {
        return currentUser != null
    }

    // Helper function to get user's name
    fun getUserName(): String {
        return currentUser?.name ?: "User"
    }
}