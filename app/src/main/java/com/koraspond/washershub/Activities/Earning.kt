package com.koraspond.washershub.Activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityEarningBinding

class Earning : AppCompatActivity() {

    lateinit var binding:ActivityEarningBinding
    var corder =0
    var tc = "0"
    var tearn = "0"
    var fearn ="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning)

        binding = DataBindingUtil.setContentView(this@Earning,R.layout.activity_earning)

        binding.backBtn.setOnClickListener {
            finish()
        }

         corder =intent.getIntExtra("customer_order",0)
        tc = intent.getStringExtra("total_commission").toString()
        tearn = intent.getStringExtra("tearn").toString()
        fearn = intent.getStringExtra("final_earning").toString()

        binding.earning.text = tearn
        binding.commision.text = corder.toString()
        binding.commisionTbp.text = tc
        binding.commEarning.text = fearn
        binding.month.text = intent.getStringExtra("name")+
                " "+"Earnings"




        setUnderline(binding.earning,binding.earning.text.toString())
        setUnderline(binding.co,binding.co.text.toString())
        setUnderline(binding.cp,binding.cp.text.toString())
        setUnderline(binding.commEarning,binding.commEarning.text.toString())
    }

    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }
}