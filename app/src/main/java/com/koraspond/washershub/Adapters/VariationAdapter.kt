package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.Earning
import com.koraspond.washershub.Activities.HistoryDetail
import com.koraspond.washershub.Models.getVariation.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.clickInterface



class VariationAdapter(private var list: ArrayList<Data>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<VariationAdapter.GroupViewHolder>() {

    private val selectedVariations = mutableSetOf<Data>()

    interface OnItemClickListener {
        fun onItemClicked(selectedVariations: List<Data>)
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.service_title)
        var tickImg: ImageView = itemView.findViewById(R.id.tick_img)

        fun bind(variation: Data) {
            title.text = variation.name
            tickImg.visibility = if (selectedVariations.contains(variation)) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                if (selectedVariations.contains(variation)) {
                    selectedVariations.remove(variation)
                } else {
                    selectedVariations.add(variation)
                }
                notifyItemChanged(adapterPosition)
                itemClickListener.onItemClicked(selectedVariations.toList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.var_rcv_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
