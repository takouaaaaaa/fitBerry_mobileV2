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
        seedFitBerryData()
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

    private fun seedFitBerryData() {
        // Create default FitBerry user
        val defaultUser = UserData(
            name = "Mehrez Hrouz",
            email = "mehrez@fitberry.com",
            gender = "Male",
            age = 25,
            weight = 70.0,
            height = 175.0,
            goal = "Maintain weight",
            activityLevel = "Moderate",
            dailyCalories = 2213,
            proteinGoal = 90,
            fatsGoal = 70,
            carbsGoal = 110
        )

        currentUser = defaultUser
        userCalorieResult = calculateUserCalorieResult(defaultUser)
        seedSampleMeals()
        seedWeeklyProgress()
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

    private fun seedWeeklyProgress() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_MONTH, -i)
            val dateStr = dateFormat.format(calendar.time)

            val caloriesConsumed = when (i) {
                0 -> 1721
                1 -> 1900
                2 -> 2200
                3 -> 2100
                4 -> 1800
                5 -> 2000
                else -> 2100
            }

            dailyProgress.add(DailyProgress(
                date = dateStr,
                caloriesConsumed = caloriesConsumed,
                caloriesGoal = 2213,
                proteinConsumed = (70 + (i * 3)).coerceAtMost(90),
                proteinGoal = 90,
                fatsConsumed = (40 + (i * 2)).coerceAtMost(70),
                fatsGoal = 70,
                carbsConsumed = (90 + (i * 5)).coerceAtMost(110),
                carbsGoal = 110
            ))
        }
    }

    // Change from private to internal or public
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
                carbsGoal = currentUser?.carbsGoal ?: 110
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

    // Change from private to internal or public
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
            carbsGoal = currentUser?.carbsGoal ?: 110
        )
    }

    fun getWeeklyProgress(): List<DailyProgress> {
        return dailyProgress.sortedBy { it.date }.takeLast(7)
    }

    // ============ USER METHODS ============

    fun saveUser(user: UserData) {
        currentUser = user
        userCalorieResult = calculateUserCalorieResult(user)
    }

    fun updateUserProfile(updatedUser: UserData) {
        currentUser = updatedUser
        userCalorieResult = calculateUserCalorieResult(updatedUser)
    }

    // REMOVED: fun getCurrentUser(): UserData? = currentUser
    // REMOVED: fun getUserCalorieResult(): CalorieResult? = userCalorieResult

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
        return if (progress.caloriesGoal > 0) {
            (progress.caloriesConsumed * 100 / progress.caloriesGoal).coerceIn(0, 100)
        } else 0
    }
}