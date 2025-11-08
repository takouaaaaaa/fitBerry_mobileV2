package com.example.fitberry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class WeightPickerActivity : AppCompatActivity() {

    private lateinit var btnKg: Button
    private lateinit var btnLbs: Button
    private lateinit var tvWeight: TextView
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageButton
    private lateinit var rulerRecycler: RecyclerView

    private var isKg = true
    private lateinit var adapter: WeightAdapter
    private var selectedWeight = 70 // valeur par défaut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_picker)

        btnKg = findViewById(R.id.btn_kg)
        btnLbs = findViewById(R.id.btn_lbs)
        tvWeight = findViewById(R.id.tv_weight)
        btnNext = findViewById(R.id.btn_next)
        btnBack = findViewById(R.id.btn_back)
        rulerRecycler = findViewById(R.id.ruler_recycler)

        // --- Adapter du "ruler" (la réglette de poids)
        val weights = (30..200).toList()
        adapter = WeightAdapter(weights) { selected ->
            selectedWeight = selected
            updateWeightDisplay(selected)
        }

        rulerRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rulerRecycler.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rulerRecycler)

        // --- Boutons de conversion
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
            // 👉 Retourne vers GoalSelectionActivity
            val intent = Intent(this, GoalSelectionActivity::class.java)
            startActivity(intent)
            finish() // facultatif : pour ne pas empiler trop d’activités
        }

        btnBack.setOnClickListener { finish() }

        // Valeur initiale
        updateWeightDisplay(selectedWeight)
    }

    private fun updateWeightDisplay(weight: Int) {
        val displayValue = if (isKg) "$weight Kg" else String.format("%.1f Lbs", weight * 2.205)
        tvWeight.text = displayValue
    }
}
