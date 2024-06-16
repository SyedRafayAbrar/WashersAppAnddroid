package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityHistoryDetailBinding
import com.koraspond.washershub.databinding.ActivityRateBinding

class RateActivity : AppCompatActivity() {
    lateinit var binding: ActivityRateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)
        binding = DataBindingUtil.setContentView(this@RateActivity,R.layout.activity_rate)
        binding.backBtn.setOnClickListener {
        finish()

        }
        }
    }
