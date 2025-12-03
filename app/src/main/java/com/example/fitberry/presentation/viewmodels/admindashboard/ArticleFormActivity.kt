package com.example.fitberry.presentation.viewmodels.admindashboard

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.R
import com.example.fitberry.data.models.Article
import com.example.fitberry.data.repository.FakeRepository
import java.util.UUID

class ArticleFormActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: TextView   // Save button is a TextView in XML

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_form)

        // Match XML IDs
        etTitle = findViewById(R.id.et_article_title)
        etContent = findViewById(R.id.et_article_desc)
        btnSave = findViewById(R.id.btn_save_article)

        val action = intent.getStringExtra("ACTION")
        val articleId = intent.getStringExtra("ARTICLE_ID")

        // Editing mode → load existing article
        if (action == "EDIT" && articleId != null) {
            val article = FakeRepository.articles.find { it.id == articleId }
            article?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        }

        // Save article
        btnSave.setOnClickListener {
            saveArticle(action, articleId)
        }
    }

    private fun saveArticle(action: String?, articleId: String?) {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        when (action) {
            "ADD" -> {
                val newArticle = Article(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content
                )
                FakeRepository.articles.add(newArticle)
                Toast.makeText(this, "Article added successfully!", Toast.LENGTH_SHORT).show()
            }

            "EDIT" -> {
                val article = FakeRepository.articles.find { it.id == articleId }
                article?.let {
                    val updatedArticle = it.copy(
                        title = title,
                        content = content
                    )
                    val index = FakeRepository.articles.indexOf(it)
                    FakeRepository.articles[index] = updatedArticle
                    Toast.makeText(this, "Article updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        finish()
    }
}
