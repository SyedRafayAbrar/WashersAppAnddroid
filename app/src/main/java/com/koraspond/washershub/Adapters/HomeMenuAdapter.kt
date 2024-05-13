package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.R


public class HomeMenuAdapter(var context: Context) :
    RecyclerView.Adapter<HomeMenuAdapter.GroupViewHolder>() {
    public class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.home_menu_rcv_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {

    }
}