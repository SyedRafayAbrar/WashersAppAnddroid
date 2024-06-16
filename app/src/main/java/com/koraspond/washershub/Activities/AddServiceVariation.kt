package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.VariationAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityAddServiceVariationBinding

class AddServiceVariation : AppCompatActivity() {
    lateinit var binding:ActivityAddServiceVariationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_variation)

        binding= DataBindingUtil.setContentView(this@AddServiceVariation,R.layout.activity_add_service_variation)

        binding.varRcv.layoutManager = LinearLayoutManager(this@AddServiceVariation)
        binding.varRcv.adapter = VariationAdapter()

         binding.backBtn.setOnClickListener {
             finish()
         }
    }
}