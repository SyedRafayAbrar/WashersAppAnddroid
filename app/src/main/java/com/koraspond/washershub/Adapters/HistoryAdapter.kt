package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.HistoryDetail
import com.koraspond.washershub.Activities.VendorORderDetails
import com.koraspond.washershub.Models.orderHistory.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.clickInterface
import com.koraspond.washershub.databinding.HistRcvItemBinding
import com.koraspond.washershub.databinding.HomeMenuRcvItemBinding


public class HistoryAdapter(var from:Int,var orderlist:ArrayList<Data>) :
    RecyclerView.Adapter<HistoryAdapter.GroupViewHolder>() {
    public class GroupViewHolder(var binding: HistRcvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderlist: java.util.ArrayList<Data>,from:Int) {

            binding.orderId.text = "Order Id #"+orderlist.get(position).unique_identifier
            binding.time.text =orderlist.get(position).order_date+" - " +orderlist.get(position).order_time_value
            binding.distance.text = orderlist.get(position).total.toString()
            binding.root.setOnClickListener {
                if(from==1) {
                    var intent = Intent(binding.root.context, HistoryDetail::class.java)
                    binding.root.context.startActivity(intent)
                }
                else{
                    var intent = Intent(binding.root.context, VendorORderDetails::class.java)
                    binding.root.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = HistRcvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderlist.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) =holder.bind(orderlist,from)
}