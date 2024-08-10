package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Models.getModel.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.ClikInt


class CategoriesFilterAdapter(
    private val context: Context,
    private val list: ArrayList<Data>,
    private val inter: ClikInt,
    private var selectedCatId: Int? // Add this parameter to handle selection state
) : RecyclerView.Adapter<CategoriesFilterAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tickImg: ImageView = itemView.findViewById(R.id.tick_img)
        var ct: ConstraintLayout = itemView.findViewById(R.id.ct)
        var title: TextView = itemView.findViewById(R.id.cat_title)
        var desc: TextView = itemView.findViewById(R.id.desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cat_rcv_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.name
        holder.desc.text = item.category_description ?: ""

        // Check if the current item is the selected one
        if (item.id == selectedCatId) {
            holder.tickImg.visibility = View.VISIBLE
        } else {
            holder.tickImg.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // Toggle selection
            if (holder.tickImg.visibility == View.GONE) {
                holder.tickImg.visibility = View.VISIBLE
                selectedCatId = item.id // Update selected category ID
                inter.onClick(position)
            } else {
                holder.tickImg.visibility = View.GONE
                selectedCatId = null // Deselect if needed
            }
            notifyDataSetChanged() // Notify that data has changed
        }
    }

    // Method to update selected category ID from outside
    fun setSelectedCatId(catId: Int?) {
        selectedCatId = catId
        notifyDataSetChanged()
    }
}
