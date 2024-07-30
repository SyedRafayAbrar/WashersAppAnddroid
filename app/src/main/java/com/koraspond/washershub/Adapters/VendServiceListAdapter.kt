package com.koraspond.washershub.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Models.getVendServices.Data
import com.koraspond.washershub.databinding.ServicesItemLayoutBinding


class VendServiceListAdapter(var serviceList:ArrayList<Data>) :
    RecyclerView.Adapter<VendServiceListAdapter.GroupViewHolder>() {
    class GroupViewHolder(var binding: ServicesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Data) {
            binding.serviceTitle.text = get.service_name
            binding.servDesc.text = get.service_description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ServicesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GroupViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int)  = holder.bind(serviceList.get(position))
}