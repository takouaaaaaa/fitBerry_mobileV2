package com.example.fitberry.data.models

import java.util.UUID

data class Article(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val author: String = "FitBerry Team",
    val publishDate: Long = System.currentTimeMillis(),
    val category: String = "Nutrition",
    val readTime: Int = 5 // minutes
) {
    fun getFormattedDate(): String {
        return android.text.format.DateFormat.format("dd MMM yyyy", publishDate).toString()
    }
}