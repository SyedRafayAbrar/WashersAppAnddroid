package com.koraspond.washershub.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.koraspond.washershub.R


public class Service_variations_Adapter(var context: Context) :
    RecyclerView.Adapter<Service_variations_Adapter.GroupViewHolder>() {
    public class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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