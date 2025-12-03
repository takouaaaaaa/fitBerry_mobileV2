package com.example.fitberry.presentation.activities.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.R
import com.example.fitberry.adapters.AgeAdapter

class AgePickerActivity : AppCompatActivity() {

    private lateinit var rvNumbers: RecyclerView
    private lateinit var btnContinue: Button
    private lateinit var adapter: AgeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_picker)

        rvNumbers = findViewById(R.id.rv_numbers)
        btnContinue = findViewById(R.id.btn_continue)

        val ages = (13..100).toList()
        adapter = AgeAdapter(ages) { selectedAge ->
            btnContinue.isEnabled = true
        }

        rvNumbers.layoutManager = LinearLayoutManager(this)
        rvNumbers.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvNumbers)

        btnContinue.setOnClickListener {
            val selectedAge = adapter.getSelectedAge()
            if (selectedAge != null) {
                // Navigate to WeightPickerActivity (next in flow)
                val intent = Intent(this, WeightPickerActivity::class.java).apply {
                    // Pass age forward
                    putExtra("AGE", selectedAge)
                    // We don't have gender or goal yet, they come later
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an age", Toast.LENGTH_SHORT).show()
            }
        }
    }
}