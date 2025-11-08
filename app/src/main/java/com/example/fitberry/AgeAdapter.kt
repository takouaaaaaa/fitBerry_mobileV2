package com.example.fitberry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AgeAdapter(
    private val ages: List<Int>,
    private val onAgeSelected: (Int) -> Unit
) : RecyclerView.Adapter<AgeAdapter.AgeViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION

    inner class AgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ageText: TextView = itemView.findViewById(R.id.tv_age)

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onAgeSelected(ages[selectedPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_age, parent, false)
        return AgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgeViewHolder, position: Int) {
        val age = ages[position]
        holder.ageText.text = age.toString()

        if (position == selectedPosition) {
            holder.ageText.setBackgroundResource(R.drawable.bg_selected_round)
            holder.ageText.setTextColor(holder.itemView.context.getColor(android.R.color.white))
        } else {
            holder.ageText.setBackgroundResource(R.drawable.bg_age_unselected)
            holder.ageText.setTextColor(holder.itemView.context.getColor(R.color.text_dark))
        }
    }

    override fun getItemCount(): Int = ages.size
}
