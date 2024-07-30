package com.koraspond.washershub.Adapters

import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.koraspond.washershub.Models.getVariation.Data
import com.koraspond.washershub.R


class Service_variations_Adapter(var list: MutableList<Data>) :
    RecyclerView.Adapter<Service_variations_Adapter.GroupViewHolder>() {
    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setUnderline(textView: TextView, text: String) {
            val content = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = content
        }
            var tickImg:ImageView
            var ct:MaterialCardView
            var varname:TextView
            var timeTv :TextView
          val tvAmount = itemView.findViewById<TextView>(R.id.tv_amount)
            init {
                tickImg = itemView.findViewById(R.id.tick_img)
                ct = itemView.findViewById(R.id.check_box)
                varname = itemView.findViewById(R.id.var_name)
                timeTv = itemView.findViewById(R.id.time_tv)
                setUnderline(timeTv,timeTv.text.toString())
                setUnderline(varname,varname.text.toString())

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.serv_var_rcv_itm, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.varname.text = list.get(position).name
        holder.tvAmount.text = list.get(position).amount.toString()
        holder.tvAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val amount = if (it.isEmpty()) 0.0 else it.toString().toDouble()
                    list.get(position).amount = amount.toDouble()// Update amount in corresponding Data object
                }
            }
        })


        holder.itemView.setOnClickListener {

            if(holder.tickImg.visibility == View.GONE){
                holder.tickImg.visibility = View.VISIBLE
                list.get(position).constraint= true
                holder.ct.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.light_green))
            }
            else{
                holder.tickImg.visibility = View.GONE
                list.get(position).constraint = false
                holder.ct.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.white))
            }
        }

    }
}