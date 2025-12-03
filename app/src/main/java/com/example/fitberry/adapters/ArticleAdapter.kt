package com.example.fitberry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitberry.data.models.Article
import com.example.fitberry.R

class ArticleAdapter(
    private var articles: MutableList<Article>,
    private val onDelete: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_article_title)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_article_desc)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title
        holder.tvDesc.text = article.content // FIXED: Use content instead of description
        holder.btnDelete.setOnClickListener {
            onDelete(article)
        }
    }

    override fun getItemCount(): Int = articles.size

    fun updateList(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
}