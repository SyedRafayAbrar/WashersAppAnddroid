package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.EarningListAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityEarningsListBinding

class EarningsList : AppCompatActivity() {
lateinit var binding:ActivityEarningsListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings_list)
        binding = DataBindingUtil.setContentView(this@EarningsList,R.layout.activity_earnings_list)
        binding.backBtn.setOnClickListener{
            finish()
        }
        binding.earnRcv.layoutManager = LinearLayoutManager(this@EarningsList)
        binding.earnRcv.adapter = EarningListAdapter()
    }
}