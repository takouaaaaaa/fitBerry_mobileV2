package com.example.fitberry

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GenderSelectionActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnNext: TextView
    private lateinit var optionFemale: LinearLayout
    private lateinit var optionMale: LinearLayout
    private lateinit var optionOther: LinearLayout
    private lateinit var radioFemale: ImageView
    private lateinit var radioMale: ImageView
    private lateinit var radioOther: ImageView

    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_selection)

        btnBack = findViewById(R.id.btn_back)
        btnNext = findViewById(R.id.btn_next)
        optionFemale = findViewById(R.id.option_female)
        optionMale = findViewById(R.id.option_male)
        optionOther = findViewById(R.id.option_other)
        radioFemale = findViewById(R.id.radio_female)
        radioMale = findViewById(R.id.radio_male)
        radioOther = findViewById(R.id.radio_other)

        btnBack.setOnClickListener { finish() }

        optionFemale.setOnClickListener { selectGender("female") }
        optionMale.setOnClickListener { selectGender("male") }
        optionOther.setOnClickListener { selectGender("other") }

        btnNext.setOnClickListener {
            // TODO: redirection ou sauvegarde du genre sélectionné
            finish()
        }
    }

    private fun selectGender(gender: String) {
        selectedGender = gender

        optionFemale.setBackgroundResource(
            if (gender == "female") R.drawable.bg_option_selected else R.drawable.bg_option_unselected
        )
        optionMale.setBackgroundResource(
            if (gender == "male") R.drawable.bg_option_selected else R.drawable.bg_option_unselected
        )
        optionOther.setBackgroundResource(
            if (gender == "other") R.drawable.bg_option_selected else R.drawable.bg_option_unselected
        )

        radioFemale.setImageResource(if (gender == "female") R.drawable.ic_radio_checked_orange else R.drawable.ic_radio_unchecked)
        radioMale.setImageResource(if (gender == "male") R.drawable.ic_radio_checked_orange else R.drawable.ic_radio_unchecked)
        radioOther.setImageResource(if (gender == "other") R.drawable.ic_radio_checked_orange else R.drawable.ic_radio_unchecked)
    }
}
