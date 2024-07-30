package com.koraspond.washershub.Activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ServiceSelectionAdapter
import com.koraspond.washershub.Adapters.TimeSelectionAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityCreateVendorOrderBinding

class CreateVendorOrder : AppCompatActivity() {
    lateinit var binding:ActivityCreateVendorOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_vendor_order)

        binding = DataBindingUtil.setContentView(this@CreateVendorOrder,R.layout.activity_create_vendor_order)

        binding.backBtn.setOnClickListener{
            finish()
        }

        setUnderline(binding.sd,binding.sd.text.toString())
        setUnderline(binding.so,binding.so.text.toString())
        setUnderline(binding.slo,binding.slo.text.toString())
        setUnderline(binding.price,binding.price.text.toString())
        setUnderline(binding.td,binding.td.text.toString())
        setUnderline(binding.til,binding.til.text.toString())


        binding.servicesRcv.layoutManager = LinearLayoutManager(this)
      //  binding.servicesRcv.adapter = ServiceSelectionAdapter(this)

        binding.timeRcv.layoutManager = GridLayoutManager(this,3)
       // binding.timeRcv.adapter = TimeSelectionAdapter(this,)


        setVehicleSpinner()





    }
    fun setVehicleSpinner(){
        var areas:ArrayList<String> = ArrayList<String>()
        areas.add("Car")
        areas.add("Bike")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehSpinner.adapter = arrayAdapter


    }
    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }
}