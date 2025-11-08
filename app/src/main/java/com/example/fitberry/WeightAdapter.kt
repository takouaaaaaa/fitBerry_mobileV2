package com.example.fitberry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeightAdapter(
    private val weights: List<Int>,
    private val onSelected: (Int) -> Unit
) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    var selectedPosition = weights.indexOf(62)

    inner class WeightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvValue: TextView = view.findViewById(R.id.tv_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruler_tick, parent, false)
        return WeightViewHolder(view)
    }

    override fun getItemCount() = weights.size

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val weight = weights[position]
        holder.tvValue.text = weight.toString()

        holder.itemView.alpha = if (position == selectedPosition) 1f else 0.4f
        holder.itemView.setOnClickListener {
            selectedPosition = position
            onSelected(weight)
            notifyDataSetChanged()
        }
    }
}
