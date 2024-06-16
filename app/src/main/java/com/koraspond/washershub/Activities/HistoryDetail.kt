package com.koraspond.washershub.Activities

import android.content.Intent
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
import com.koraspond.washershub.databinding.ActivityHistoryDetailBinding

class HistoryDetail : AppCompatActivity() {

    lateinit var binding:ActivityHistoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
        binding = DataBindingUtil.setContentView(this@HistoryDetail,R.layout.activity_history_detail)
        setUnderline(binding.location,"Gulshan e iqbal karachi ")
        setUnderline(binding.callNum,"Tap to Call Vendor")
        setUnderline(binding.orderTitle,"Order Date :")
        setUnderline(binding.ordDate,"12/24/2024")
        setUnderline(binding.dayTitle,"Order Day :")
        setUnderline(binding.dayDate,"Monday")
        setUnderline(binding.timeTitle,"Order Time :")
        setUnderline(binding.timeDate,"02:00 P.m - 03:00 A.M")
        setUnderline(binding.serTitle,"Services Selected")

        binding.materialButton.setOnClickListener{
            var intent = Intent(this@HistoryDetail,RateActivity::class.java)
            startActivity(intent)
        }
binding.backBtn.setOnClickListener{
    finish()
}


    }

    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }
}