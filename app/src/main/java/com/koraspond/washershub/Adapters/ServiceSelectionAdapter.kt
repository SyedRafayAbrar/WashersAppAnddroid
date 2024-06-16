package com.koraspond.washershub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Models.vendorDetails.Service
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.clickInterface

class ServiceSelectionAdapter(var context: Context, var servlist: ArrayList<Service>,var clickInterface: clickInterface) :
    RecyclerView.Adapter<ServiceSelectionAdapter.GroupViewHolder>() {

    // Variable to keep track of the currently selected position
    private var selectedPosition = -1

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tickImg: ImageView = itemView.findViewById(R.id.tick_img)
        var servtitle: TextView = itemView.findViewById(R.id.service_title)
        var price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.services_layout_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return servlist.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.servtitle.text = servlist[position].service_name
        holder.price.text = servlist[position].service_charges.toString()

        // Set the visibility of the tick image based on the selected position
        if (selectedPosition == position) {
            holder.tickImg.visibility = View.VISIBLE
        } else {
            holder.tickImg.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // Update the selected position
            val previousPosition = selectedPosition
            selectedPosition = position
            clickInterface.onClick(position)
            // Notify the adapter to update the previous and new selected positions
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

        }
    }
}
