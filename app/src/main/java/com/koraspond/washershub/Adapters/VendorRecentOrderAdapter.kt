package com.koraspond.washershub.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.VendorORderDetails
import com.koraspond.washershub.databinding.HistRcvItemBinding


class VendorRecentOrderAdapter(var from:Int, var orderlist:ArrayList<com.koraspond.washershub.Models.vendorModels.getVendorOrder.Data>) :
    RecyclerView.Adapter<VendorRecentOrderAdapter.GroupViewHolder>() {
    class GroupViewHolder(var binding: HistRcvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderlist: java.util.ArrayList<com.koraspond.washershub.Models.vendorModels.getVendorOrder.Data>,from:Int) {

            binding.orderId.text = "Order Id #"+orderlist.get(position).unique_identifier
            binding.status.text = orderlist.get(position).latest_status.name
            binding.time.text =orderlist.get(position).order_date+" - " +orderlist.get(position).order_time_value
            binding.distance.text = orderlist.get(position).total.toString()
            binding.location.text = orderlist.get(position).vendor.address
            binding.root.setOnClickListener {
//                if(from==1) {
//                    var intent = Intent(binding.root.context, HistoryDetail::class.java)
//                    intent.putExtra("id",orderlist.get(position).unique_identifier)
//                    binding.root.context.startActivity(intent)
//                }
//                else{
                    var intent = Intent(binding.root.context, VendorORderDetails::class.java)
                intent.putExtra("id",orderlist.get(position).unique_identifier)

                binding.root.context.startActivity(intent)
//                }
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