package com.koraspond.washershub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Activities.CreateService
import com.koraspond.washershub.Adapters.VendServiceListAdapter
import com.koraspond.washershub.Models.getVendorServices.Data
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.databinding.ActivityVendorServicesListBinding

class VendorServicesList : AppCompatActivity() {

    lateinit var binding:ActivityVendorServicesListBinding

    lateinit var viewModel: VendoApisViewModel
    lateinit var adapter: VendServiceListAdapter

    lateinit var serviceList:ArrayList<com.koraspond.washershub.Models.getVendServices.Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_services_list)


        binding = DataBindingUtil.setContentView(this@VendorServicesList,R.layout.activity_vendor_services_list)

        val vendorRepo = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(vendorRepo))
            .get(VendoApisViewModel::class.java)
         serviceList = ArrayList()
        binding.backBtn.setOnClickListener {
            finish()
        }
        var progress = ProgressDialog(this@VendorServicesList)
        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        binding.servRcv.layoutManager = LinearLayoutManager(this@VendorServicesList)
        adapter = VendServiceListAdapter(serviceList)
        binding.servRcv.adapter = adapter


        binding.createServBtn.setOnClickListener {
            startActivity(Intent(this@VendorServicesList,CreateService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchRecentOrder()
    }

    private fun fetchRecentOrder() {
        serviceList.clear()

        val token = UserInfoPreference(this).getStr("token").toString()
        val id = UserInfoPreference(this).getStr("vid").toString()
        viewModel.getServices(token, id).observe(this@VendorServicesList, Observer { resource ->
            resource?.let {                if (it.data != null) {

                    serviceList.addAll(it.data.data)

                    adapter.notifyDataSetChanged()


                } else {
                    Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }
}