package com.example.fitberry

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class GoalSelectionActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnNext: TextView

    private lateinit var optionLose: LinearLayout
    private lateinit var optionMaintain: LinearLayout
    private lateinit var optionGain: LinearLayout

    private lateinit var radioLose: ImageView
    private lateinit var radioMaintain: ImageView
    private lateinit var radioGain: ImageView

    private var selectedGoal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_selection)

        btnBack = findViewById(R.id.btn_back)
        btnNext = findViewById(R.id.btn_next)

        optionLose = findViewById(R.id.option_lose)
        optionMaintain = findViewById(R.id.option_maintain)
        optionGain = findViewById(R.id.option_gain)

        radioLose = findViewById(R.id.radio_lose)
        radioMaintain = findViewById(R.id.radio_maintain)
        radioGain = findViewById(R.id.radio_gain)

        btnBack.setOnClickListener { finish() }

        optionLose.setOnClickListener { selectGoal("Lose weight", optionLose, radioLose) }
        optionMaintain.setOnClickListener { selectGoal("Maintain weight", optionMaintain, radioMaintain) }
        optionGain.setOnClickListener { selectGoal("Gain weight", optionGain, radioGain) }

        btnNext.setOnClickListener {
            if (selectedGoal != null) {
                val intent = Intent(this, WeightPickerActivity::class.java)
                intent.putExtra("goal", selectedGoal)
                startActivity(intent)
            }
        }
    }

    private fun selectGoal(goal: String, selectedLayout: LinearLayout, selectedRadio: ImageView) {
        selectedGoal = goal

        val allOptions = listOf(optionLose, optionMaintain, optionGain)
        val allRadios = listOf(radioLose, radioMaintain, radioGain)

        for (i in allOptions.indices) {
            allOptions[i].background = ContextCompat.getDrawable(this, R.drawable.bg_option_unselected)
            allRadios[i].setImageResource(R.drawable.ic_radio_unchecked)
        }

        selectedLayout.background = ContextCompat.getDrawable(this, R.drawable.bg_option_selected_orange)
        selectedRadio.setImageResource(R.drawable.ic_radio_checked_orange)
        btnNext.setOnClickListener {
            val intent = Intent(this, GenderSelectionActivity::class.java)
            startActivity(intent)
        }

    }
}
