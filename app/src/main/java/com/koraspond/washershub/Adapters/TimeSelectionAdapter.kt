package com.koraspond.washershub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.koraspond.washershub.R

class TimeSelectionAdapter(private val context: Context, private val list: ArrayList<String>) :
    RecyclerView.Adapter<TimeSelectionAdapter.GroupViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: MaterialCardView = itemView.findViewById(R.id.brand_cv)
        val check: RelativeLayout = itemView.findViewById(R.id.check)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_select_layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.itemView.isSelected = position == selectedItemPosition
        holder.time.text = list[position]

        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }

        if (position == selectedItemPosition) {
            holder.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pgreen))
            holder.check.setBackgroundColor(ContextCompat.getColor(context, R.color.pgreen))
        } else {
            holder.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.check.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    // Public method to get the selected time slot
    fun getSelectedTimeSlot(): String? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            list[selectedItemPosition]
        } else {
            null
        }
    }
}
