package com.koraspond.washershub.Adapters

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Models.getVendorsModel.Item
import com.koraspond.washershub.Utils.clickInterfaceVendor
import com.koraspond.washershub.databinding.HomeMenuRcvItemBinding

class HomeMenuAdapter(
    var context: Context,
    var list: ArrayList<Item>,
    var inter: clickInterfaceVendor,
    var userLat: Double,
    var userLng: Double
) : RecyclerView.Adapter<HomeMenuAdapter.GroupViewHolder>() {

    class GroupViewHolder(var binding: HomeMenuRcvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Item, inter: clickInterfaceVendor, userLat: Double, userLng: Double) {
            binding.menuName.text = get.shop_name.toString()
            binding.location.text = get.address
            binding.time.text = get.start_time + "-" + get.end_time

            val distance = calculateDistance(userLat, userLng, get.lat, get.long)
            binding.distance.text = "%.2f km".format(distance)

            binding.root.setOnClickListener {
                inter.onClick(adapterPosition,distance)
            }
        }

        private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
            val results = FloatArray(1)
            Location.distanceBetween(lat1, lng1, lat2, lng2, results)
            return (results[0] / 1000).toDouble() // Convert meters to kilometers
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = HomeMenuRcvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(list[position], inter, userLat, userLng)
    }
}
