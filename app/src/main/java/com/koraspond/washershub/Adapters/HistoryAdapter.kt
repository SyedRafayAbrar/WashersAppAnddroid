package com.koraspond.washershub.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.HistoryDetail
import com.koraspond.washershub.Activities.VendorORderDetails
import com.koraspond.washershub.Models.orderHistory.Item
import com.koraspond.washershub.databinding.HistRcvItemBinding


class HistoryAdapter(var from:Int,var orderlist:ArrayList<Item>) :
    RecyclerView.Adapter<HistoryAdapter.GroupViewHolder>() {
    class GroupViewHolder(var binding: HistRcvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderlist: java.util.ArrayList<Item >,from:Int) {

            binding.orderId.text = "Order Id #"+orderlist.get(position).unique_identifier
            binding.status.text = orderlist.get(position).latest_status.name
            binding.time.text =orderlist.get(position).order_date+" - " +orderlist.get(position).order_time_value
            binding.distance.text = orderlist.get(position).total.toString()
            binding.location.text = orderlist.get(position).vendor.shop_name

            binding.root.setOnClickListener {
                if(from==1) {
                    var intent = Intent(binding.root.context, HistoryDetail::class.java)
                    intent.putExtra("id",orderlist.get(position).unique_identifier)
                    intent.putExtra("oid",orderlist.get(position).id.toString())
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