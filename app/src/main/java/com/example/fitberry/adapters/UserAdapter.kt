package com.example.fitberry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.R
import com.example.fitberry.data.models.User

class UserAdapter(
    private val items: MutableList<User>,
    private val onEdit: (User) -> Unit,
    private val onDelete: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tv_user_name)
        val email: TextView = view.findViewById(R.id.tv_user_email)
        val role: TextView = view.findViewById(R.id.tv_user_role)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit_user)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val u = items[position]
        holder.name.text = u.name
        holder.email.text = u.email
        holder.role.text = u.role
        holder.btnEdit.setOnClickListener { onEdit(u) }
        holder.btnDelete.setOnClickListener { onDelete(u) }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<User>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
