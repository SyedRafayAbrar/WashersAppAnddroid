package com.koraspond.washershub.Activities

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.Service_variations_Adapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityCreateServiceBinding

class CreateService : AppCompatActivity() {
    lateinit var binding:ActivityCreateServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_service)
        binding = DataBindingUtil.setContentView(this@CreateService,R.layout.activity_create_service)


        binding.varRcv.layoutManager = LinearLayoutManager(this)
        binding.varRcv.adapter = Service_variations_Adapter(this@CreateService)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.addVar.setOnClickListener{
            startActivity(Intent(this@CreateService,AddServiceVariation::class.java))
        }

        setCatSpinner()
        setUnderline(binding.sel,binding.sel.text.toString())
        setUnderline( binding.addVar,binding.addVar.text.toString())
        setUnderline( binding.noteAdmin,binding.noteAdmin.text.toString())
    }
    fun setCatSpinner(){
        var areas:ArrayList<String> = ArrayList<String>()
        areas.add("Wash")
        areas.add("Full Service")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.catSpinner.setAdapter(arrayAdapter)


    }
    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }
}