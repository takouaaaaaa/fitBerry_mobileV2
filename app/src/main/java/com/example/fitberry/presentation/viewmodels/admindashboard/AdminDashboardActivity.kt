package com.example.fitberry.presentation.viewmodels.admindashboard

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fitberry.data.repository.FakeRepository
import com.example.fitberry.R
import com.google.android.material.button.MaterialButton

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var tvUsersCount: TextView
    private lateinit var tvArticlesCount: TextView
    private lateinit var btnLogout: MaterialButton
    private lateinit var btnAddUser: MaterialButton
    private lateinit var btnAddArticle: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Initialize views
        tvUsersCount = findViewById(R.id.tv_users_count)
        tvArticlesCount = findViewById(R.id.tv_articles_count)
        btnLogout = findViewById(R.id.btn_logout)
        btnAddUser = findViewById(R.id.btn_add_user)
        btnAddArticle = findViewById(R.id.btn_add_article)

        // CORRECTED: These are LinearLayout, not ConstraintLayout
        findViewById<LinearLayout>(R.id.card_users).setOnClickListener {
            navigateToUsersList()
        }

        findViewById<LinearLayout>(R.id.card_articles).setOnClickListener {
            navigateToArticlesList()
        }

        findViewById<LinearLayout>(R.id.card_manage_users).setOnClickListener {
            navigateToUsersList()
        }

        findViewById<LinearLayout>(R.id.card_manage_articles).setOnClickListener {
            navigateToArticlesList()
        }

        // Set up click listeners for buttons
        btnAddUser.setOnClickListener {
            navigateToAddUser()
        }

        btnAddArticle.setOnClickListener {
            navigateToAddArticle()
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStats()
    }

    private fun updateStats() {
        // CORRECTED: Use adminUsers instead of users
        tvUsersCount.text = FakeRepository.adminUsers.size.toString()
        tvArticlesCount.text = FakeRepository.articles.size.toString()
    }

    private fun navigateToUsersList() {
        startActivity(Intent(this, UsersListActivity::class.java))
    }

    private fun navigateToArticlesList() {
        startActivity(Intent(this, ArticleListActivity::class.java))
    }

    private fun navigateToAddUser() {
        val intent = Intent(this, UserFormActivity::class.java)
        intent.putExtra(UsersListActivity.EXTRA_ACTION, UsersListActivity.ACTION_ADD)
        startActivity(intent)
    }

    private fun navigateToAddArticle() {
        val intent = Intent(this, ArticleFormActivity::class.java)
        intent.putExtra("ACTION", "ADD")
        startActivity(intent)
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                // Clear any session/token here
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}