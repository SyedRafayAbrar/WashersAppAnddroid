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
import com.koraspond.washershub.R


class CategoriesFilterAdapter(var context: Context) :
    RecyclerView.Adapter<CategoriesFilterAdapter.GroupViewHolder>() {
    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tickImg:ImageView
            var ct:ConstraintLayout
            init {
                tickImg = itemView.findViewById(R.id.tick_img)
                ct = itemView.findViewById(R.id.ct)

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.cat_rcv_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {

        holder.itemView.setOnClickListener {

            if(holder.tickImg.visibility == View.GONE){
                holder.tickImg.visibility = View.VISIBLE
                holder.ct.setBackgroundColor(context.resources.getColor(R.color.light_green))
            }
            else{
                holder.tickImg.visibility = View.GONE
                holder.ct.setBackgroundColor(context.resources.getColor(R.color.white))
            }
        }

    }
}