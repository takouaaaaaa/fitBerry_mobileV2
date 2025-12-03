package com.example.fitberry.presentation.activities.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.R
import com.example.fitberry.data.models.UserData
import com.example.fitberry.data.repository.FakeRepository
import com.example.fitberry.presentation.activities.main.DashboardActivity
import com.example.fitberry.utils.calculators.CalorieCalculator

class WelcomeProfileUpdatedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_profile_updated)

        // Get data from previous activities
        val gender = intent.getStringExtra("GENDER") ?: "Male"
        val age = intent.getIntExtra("AGE", 25)
        val weight = intent.getDoubleExtra("WEIGHT", 70.0)
        val goal = intent.getStringExtra("GOAL") ?: "Maintain weight"

        // Display gender
        val tvGender = findViewById<TextView>(R.id.tv_gender)
        tvGender.text = "Gender: $gender"

        // Display other info if needed
        val tvAge = findViewById<TextView>(R.id.tv_age)
        val tvWeight = findViewById<TextView>(R.id.tv_weight)
        val tvGoal = findViewById<TextView>(R.id.tv_goal)

        tvAge.text = "Age: $age"
        tvWeight.text = "Weight: ${weight}kg"
        tvGoal.text = "Goal: $goal"

        // Create and save user
        val user = createUser(gender, age, weight, goal)
        FakeRepository.saveUser(user)

        // Continue button
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUser(gender: String, age: Int, weight: Double, goal: String): UserData {
        // Calculate calories and macros
        val bmr = CalorieCalculator.calculateBMR(gender, weight, 175.0, age)
        val tdee = CalorieCalculator.calculateTDEE(bmr, "Moderate")
        val targetCalories = CalorieCalculator.calculateGoalCalories(tdee, goal)
        val (protein, fats, carbs) = CalorieCalculator.calculateMacros(targetCalories, weight)

        return UserData(
            name = "Mehrez Hrouz",
            email = "mehrez@fitberry.com",
            gender = gender,
            age = age,
            weight = weight,
            height = 175.0,
            goal = goal,
            activityLevel = "Moderate",
            dailyCalories = targetCalories.toInt(),
            proteinGoal = protein,
            fatsGoal = fats,
            carbsGoal = carbs
        )
    }
}