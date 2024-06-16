package com.koraspond.washershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Activities.CreateService
import com.koraspond.washershub.Adapters.VendServiceListAdapter
import com.koraspond.washershub.databinding.ActivityVendorServicesListBinding

class VendorServicesList : AppCompatActivity() {

    lateinit var binding:ActivityVendorServicesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_services_list)

        binding = DataBindingUtil.setContentView(this@VendorServicesList,R.layout.activity_vendor_services_list)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.servRcv.layoutManager = LinearLayoutManager(this@VendorServicesList)
        binding.servRcv.adapter = VendServiceListAdapter()

        binding.createServBtn.setOnClickListener {
            startActivity(Intent(this@VendorServicesList,CreateService::class.java))
        }
    }
}