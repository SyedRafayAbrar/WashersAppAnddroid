package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Activities.Earning
import com.koraspond.washershub.Activities.EarningsList
import com.koraspond.washershub.Activities.HistoryDetail
import com.koraspond.washershub.Models.getEarnings.list
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.clickInterface
import java.nio.DoubleBuffer


class EarningListAdapter(var yearlist:ArrayList<String>,var earningsList: ArrayList<list>,var finalEarning:String,var sel:Int) :
    RecyclerView.Adapter<EarningListAdapter.GroupViewHolder>() {
    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var monthname:TextView = itemView.findViewById(R.id.month)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.earning_list_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return yearlist.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
            holder.monthname.text = yearlist.get(position)
        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView.context, Earning::class.java)
            intent.putExtra("customer_order",earningsList.get(position).total_customer_orders)
            intent.putExtra("total_commission",earningsList.get(position).total_commission)
            intent.putExtra("tearn",earningsList.get(position).total_earnings)
            intent.putExtra("final_earning",finalEarning)
            if(sel==0){
              intent.putExtra("name",earningsList.get(position).month)
            }
            else{
                intent.putExtra("name",earningsList.get(position).year.toString())
            }
            holder.itemView.context.startActivity(intent)
        }

    }
}