package com.koraspond.washershub.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.HomeMenuAdapter
import com.koraspond.washershub.Fragments.CategoriesFragment
import com.koraspond.washershub.Fragments.LoginFragment
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this@HomeActivity,R.layout.activity_home)

        binding.catRcv.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.catRcv.adapter = HomeMenuAdapter(this@HomeActivity)

        binding.filter.setOnClickListener {
            supportFragmentManager.
            beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .addToBackStack(null) .replace(R.id.root, CategoriesFragment()).commit()
        }

        setAreaSpinner()
        setSortSpinner()




    }

    fun setSortSpinner(){

        var sort:ArrayList<String> = ArrayList<String>()
        sort.add("Sort")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriesSpinner.setAdapter(arrayAdapter)
    }
    fun setAreaSpinner(){
        var areas:ArrayList<String> = ArrayList<String>()
        areas.add("Area")
        areas.add("Malir")
        areas.add("Gulshan")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areaSpinner.setAdapter(arrayAdapter)


    }
}