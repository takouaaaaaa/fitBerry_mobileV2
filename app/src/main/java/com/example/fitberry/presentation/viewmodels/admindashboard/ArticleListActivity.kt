package com.example.fitberry.presentation.viewmodels.admindashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.R
import com.example.fitberry.adapters.ArticleAdapter
import com.example.fitberry.data.models.Article
import com.example.fitberry.data.repository.FakeRepository

class ArticleListActivity : AppCompatActivity() {

    private lateinit var rvArticles: RecyclerView
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        rvArticles = findViewById(R.id.rv_articles)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = ArticleAdapter(
            FakeRepository.articles.toMutableList(),
            onDelete = { article -> deleteArticle(article) }
        )

        rvArticles.layoutManager = LinearLayoutManager(this)
        rvArticles.adapter = adapter
    }

    private fun deleteArticle(article: Article) {
        AlertDialog.Builder(this)
            .setTitle("Delete Article")
            .setMessage("Are you sure you want to delete this article?")
            .setPositiveButton("Delete") { _, _ ->
                FakeRepository.articles.remove(article)
                adapter.updateList(FakeRepository.articles)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList(FakeRepository.articles)
    }
}
