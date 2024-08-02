package com.koraspond.washershub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Models.getCitieis.Data
import com.koraspond.washershub.R

class ReasAdapter(
    private val context: Context,
    private val lis: ArrayList<Data>,
    private val onItemClick: (String,String) -> Unit // Callback for item click
) : RecyclerView.Adapter<ReasAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var areaTv: TextView = itemView.findViewById(R.id.area_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.area_rcv_item_layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val area = lis[position]
        holder.areaTv.text = area.name

        holder.itemView.setOnClickListener {
            onItemClick(lis.get(position).id.toString(),lis.get(position).name) // Pass the selected area ID
        }
    }
}
