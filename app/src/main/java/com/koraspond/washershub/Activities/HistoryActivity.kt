package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.HistoryAdapter
import com.koraspond.washershub.Adapters.ServiceSelectionAdapter
import com.koraspond.washershub.Models.orderHistory.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepo
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    lateinit var binding:ActivityHistoryBinding
    lateinit var viewModel: VendorViewModel
    lateinit var ordrlist:ArrayList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        binding = DataBindingUtil.setContentView(this@HistoryActivity,R.layout.activity_history)
        val vendorRepo = VendorRepo.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(vendorRepo))
            .get(VendorViewModel::class.java)
        ordrlist = ArrayList()

        binding.histRcv.layoutManager = LinearLayoutManager(this)

        var progress = ProgressDialog(this@HistoryActivity)
        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }


        binding.back.setOnClickListener{
            finish()
        }
        setSortSpinner()
        fetchHistory()
    }
    private fun fetchHistory() {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getHistory(token).observe(this@HistoryActivity, Observer { resource ->
            resource?.let {
                if (it.data != null) {

                    ordrlist.addAll(it.data.data)

                    binding.histRcv.adapter = HistoryAdapter(1,ordrlist)


                } else {
                    Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
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