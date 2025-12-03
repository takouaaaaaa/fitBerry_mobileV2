package com.example.fitberry.presentation.viewmodels.admindashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.R
import com.example.fitberry.adapters.UserAdapter
import com.example.fitberry.presentation.viewmodels.admindashboard.UserFormActivity
import com.example.fitberry.data.models.User
import com.example.fitberry.data.repository.FakeRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsersListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACTION = "ACTION"
        const val ACTION_ADD = "ADD"
        const val ACTION_EDIT = "EDIT"
    }

    private lateinit var rvUsers: RecyclerView
    private lateinit var fabAddUser: FloatingActionButton
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        rvUsers = findViewById(R.id.rv_users)
        fabAddUser = findViewById(R.id.fab_add_user)

        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            FakeRepository.adminUsers.toMutableList(),
            onDelete = { user -> deleteUser(user) },
            onEdit = { user -> editUser(user) }
        )

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = adapter
    }

    private fun setupListeners() {
        fabAddUser.setOnClickListener {
            val intent = Intent(this, UserFormActivity::class.java)
            intent.putExtra(EXTRA_ACTION, ACTION_ADD)
            startActivity(intent)
        }
    }

    private fun deleteUser(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete ${user.name}?")
            .setPositiveButton("Delete") { _, _ ->
                FakeRepository.deleteAdminUser(user.id)
                adapter.updateList(FakeRepository.adminUsers)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun editUser(user: User) {
        val intent = Intent(this, UserFormActivity::class.java)
        intent.putExtra(EXTRA_ACTION, ACTION_EDIT)
        intent.putExtra("USER_ID", user.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList(FakeRepository.adminUsers)
    }
}