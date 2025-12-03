package com.example.fitberry.presentation.activities.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.R
import com.example.fitberry.adapters.WeightAdapter

class WeightPickerActivity : AppCompatActivity() {

    private lateinit var btnKg: Button
    private lateinit var btnLbs: Button
    private lateinit var tvWeight: TextView
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageButton
    private lateinit var rulerRecycler: RecyclerView

    private var isKg = true
    private lateinit var adapter: WeightAdapter
    private var selectedWeight = 70 // default value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_picker)

        btnKg = findViewById(R.id.btn_kg)
        btnLbs = findViewById(R.id.btn_lbs)
        tvWeight = findViewById(R.id.tv_weight)
        btnNext = findViewById(R.id.btn_next)
        btnBack = findViewById(R.id.btn_back)
        rulerRecycler = findViewById(R.id.ruler_recycler)

        // --- Weight ruler adapter
        val weights = (30..200).toList()
        adapter = WeightAdapter(weights) { selected ->
            selectedWeight = selected
            updateWeightDisplay(selected)
        }

        rulerRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rulerRecycler.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rulerRecycler)

        // --- Conversion buttons
        btnKg.setOnClickListener {
            isKg = true
            btnKg.setBackgroundResource(R.drawable.bg_unit_selected)
            btnLbs.setBackgroundResource(R.drawable.bg_unit_unselected)
            updateWeightDisplay(selectedWeight)
        }

        btnLbs.setOnClickListener {
            isKg = false
            btnLbs.setBackgroundResource(R.drawable.bg_unit_selected)
            btnKg.setBackgroundResource(R.drawable.bg_unit_unselected)
            updateWeightDisplay(selectedWeight)
        }

        // --- Navigation
        btnNext.setOnClickListener {
            // Navigate to AgePickerActivity with all data
            val intent = Intent(this, AgePickerActivity::class.java).apply {
                putExtra("WEIGHT", selectedWeight.toDouble())
                // Pass through existing data
                putExtra("GOAL", this@WeightPickerActivity.intent.getStringExtra("GOAL"))
            }
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }

        // Initial value
        updateWeightDisplay(selectedWeight)

        // Set initial button states
        btnKg.setBackgroundResource(R.drawable.bg_unit_selected)
        btnLbs.setBackgroundResource(R.drawable.bg_unit_unselected)
    }

    private fun updateWeightDisplay(weight: Int) {
        val displayValue = if (isKg) "$weight Kg" else String.format("%.1f Lbs", weight * 2.205)
        tvWeight.text = displayValue
    }
}