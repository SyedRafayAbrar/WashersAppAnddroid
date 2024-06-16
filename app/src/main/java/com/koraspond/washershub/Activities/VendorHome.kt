package com.koraspond.washershub.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.koraspond.washershub.Adapters.HistoryAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.VendorServicesList
import com.koraspond.washershub.databinding.ActivityVendorHomeBinding

class VendorHome : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityVendorHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_home)
        binding = DataBindingUtil.setContentView(this@VendorHome,R.layout.activity_vendor_home)

        binding.createOrder.setOnClickListener {
            var intent = Intent(this@VendorHome,CreateVendorOrder::class.java)
            startActivity(intent)
        }

        binding.histRcv.layoutManager = LinearLayoutManager(this)
      //  binding.histRcv.adapter = HistoryAdapter(2)
        binding.menu.setOnClickListener {
            if (!binding.myDrawerLayout.isDrawerOpen(Gravity.LEFT)) binding.myDrawerLayout.openDrawer(
                Gravity.LEFT) else binding.myDrawerLayout.closeDrawer(
                Gravity.RIGHT
            )
        }


        binding.nav.setNavigationItemSelectedListener(this)
        binding.nav.menu.findItem(R.id.home).isChecked = true

        setSortSpinner()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.earning -> {
                var intent = Intent(this@VendorHome, EarningsList::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.services -> {
                var intent = Intent(this@VendorHome, VendorServicesList::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }

        }
        return false
    }

    fun setSortSpinner(){
        var areas:ArrayList<String> = ArrayList<String>()
        areas.add("Ascending")
        areas.add("Descending")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.setAdapter(arrayAdapter)


    }
}