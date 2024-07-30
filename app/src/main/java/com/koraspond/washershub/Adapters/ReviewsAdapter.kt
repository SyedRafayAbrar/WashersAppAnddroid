package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.HistoryDetail
import com.koraspond.washershub.Models.getReview.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.clickInterface


class ReviewsAdapter(var list:ArrayList<Data>) :
    RecyclerView.Adapter<ReviewsAdapter.GroupViewHolder>() {
    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var reviewsTv =itemView.findViewById<TextView>(R.id.review_tv)
        var rat_bar =itemView.findViewById<RatingBar>(R.id.rat_bar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reviews_view_rcv_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.reviewsTv .text = list.get(position).review
        holder.rat_bar.rating = list.get(position).rating.toFloat()


    }
}