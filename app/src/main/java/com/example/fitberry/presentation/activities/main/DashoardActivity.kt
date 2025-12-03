package com.example.fitberry.presentation.activities.main

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitberry.R
import com.example.fitberry.data.models.Meal
import com.example.fitberry.data.repository.FakeRepository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var progressCalories: ProgressBar
    private lateinit var progressProtein: ProgressBar
    private lateinit var progressFats: ProgressBar
    private lateinit var progressCarbs: ProgressBar
    private lateinit var tvCalories: TextView
    private lateinit var tvTodayCalories: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvFats: TextView
    private lateinit var tvCarbs: TextView
    private lateinit var barChart: BarChart
    private lateinit var layoutRecentMeals: LinearLayout
    private lateinit var btnAddMeal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        progressCalories = findViewById(R.id.progress_calories)
        progressProtein = findViewById(R.id.progress_protein)
        progressFats = findViewById(R.id.progress_fats)
        progressCarbs = findViewById(R.id.progress_carbs)
        tvCalories = findViewById(R.id.tv_calories)
        tvTodayCalories = findViewById(R.id.tv_today_calories)
        tvProtein = findViewById(R.id.tv_protein)
        tvFats = findViewById(R.id.tv_fats)
        tvCarbs = findViewById(R.id.tv_carbs)
        barChart = findViewById(R.id.bar_chart)
        layoutRecentMeals = findViewById(R.id.layout_recent_meals)
        btnAddMeal = findViewById(R.id.btn_add_meal)

        setupUI()
        setupChart()
        setupRecentMeals()
        setupAddMealButton()
    }

    private fun setupUI() {
        // Current time
        val timeTextView = findViewById<TextView>(R.id.tv_time)
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        timeTextView.text = currentTime

        // Welcome message
        val welcomeTextView = findViewById<TextView>(R.id.tv_welcome_name)
        val user = FakeRepository.currentUser
        welcomeTextView.text = "Welcome\n${user?.name ?: "User"}"

        // Update progress bars and macros
        updateStats()
    }

    private fun updateStats() {
        val progress = FakeRepository.getTodayProgress()

        // Calories
        val caloriePercent = progress.getCaloriePercentage()
        progressCalories.progress = caloriePercent
        tvCalories.text = "${progress.caloriesConsumed} Kcal\nof ${progress.caloriesGoal} kcal"
        tvTodayCalories.text = "Today Calorie: ${progress.caloriesConsumed}"

        // Protein
        progressProtein.progress = progress.getProteinPercentage()
        tvProtein.text = "${progress.proteinConsumed}/${progress.proteinGoal}g"

        // Fats
        progressFats.progress = progress.getFatsPercentage()
        tvFats.text = "${progress.fatsConsumed}/${progress.fatsGoal}g"

        // Carbs
        progressCarbs.progress = progress.getCarbsPercentage()
        tvCarbs.text = "${progress.carbsConsumed}/${progress.carbsGoal}g"
    }

    private fun setupChart() {
        val weeklyProgress = FakeRepository.getWeeklyProgress()

        val entries = weeklyProgress.mapIndexed { index, day ->
            BarEntry(index.toFloat(), day.caloriesConsumed.toFloat())
        }

        val dataSet = BarDataSet(entries, "Calories").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 12f
        }

        val barData = BarData(dataSet).apply { barWidth = 0.5f }

        barChart.apply {
            data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            animateY(1000)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 7
                valueFormatter = IndexAxisValueFormatter(arrayOf("S", "M", "T", "W", "T", "F", "S"))
                textSize = 12f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
                axisMaximum = 2500f
                granularity = 500f
                textSize = 12f
            }

            axisRight.isEnabled = false
            invalidate()
        }
    }

    private fun setupRecentMeals() {
        layoutRecentMeals.removeAllViews()
        val todayMeals = FakeRepository.getTodayMeals()

        if (todayMeals.isEmpty()) {
            val noMealsTextView = TextView(this).apply {
                text = "No meals logged today"
                setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.text_gray))
                textSize = 14f
                setPadding(16, 16, 16, 16)
            }
            layoutRecentMeals.addView(noMealsTextView)
        } else {
            todayMeals.forEach { meal ->
                val mealView = layoutInflater.inflate(R.layout.item_meal, null)
                mealView.findViewById<TextView>(R.id.tv_meal_name).text = meal.name
                mealView.findViewById<TextView>(R.id.tv_meal_type).text = meal.mealType.displayName
                mealView.findViewById<TextView>(R.id.tv_meal_calories).text = "${meal.calories} kcal"
                mealView.findViewById<TextView>(R.id.tv_meal_macros).text =
                    "P:${meal.protein}g F:${meal.fats}g C:${meal.carbs}g"
                layoutRecentMeals.addView(mealView)
            }
        }
    }

    private fun setupAddMealButton() {
        btnAddMeal.setOnClickListener {
            val sampleMeal = Meal(
                name = "Sample Snack",
                calories = 200,
                protein = 5,
                fats = 10,
                carbs = 25,
                mealType = Meal.MealType.SNACK
            )
            FakeRepository.addMeal(sampleMeal)
            updateStats()
            setupRecentMeals()
            setupChart()
            Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStats()
        setupChart()
        setupRecentMeals()
    }
}
