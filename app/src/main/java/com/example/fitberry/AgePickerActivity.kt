package com.example.fitberry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

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
            // Exemple : aller vers une autre activité
            val intent = Intent(this, WeightPickerActivity::class.java)
            startActivity(intent)
        }
    }
}
