package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.EarningListAdapter
import com.koraspond.washershub.Adapters.VariationAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.databinding.ActivityEarningsListBinding

import android.widget.CompoundButton

class EarningsList : AppCompatActivity() {
    lateinit var binding: ActivityEarningsListBinding
    private lateinit var viewModel: VendoApisViewModel
    private lateinit var adapter: EarningListAdapter
    private lateinit var earningList: ArrayList<com.koraspond.washershub.Models.getEarnings.list>
    private lateinit var monthList: ArrayList<String>
    var finalEarning: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings_list)
        binding = DataBindingUtil.setContentView(this@EarningsList, R.layout.activity_earnings_list)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.earnRcv.layoutManager = LinearLayoutManager(this@EarningsList)
        monthList = ArrayList()
        earningList = ArrayList()

        val vendorRepo = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(vendorRepo))
            .get(VendoApisViewModel::class.java)

        val progress = ProgressDialog(this@EarningsList)
        viewModel.isLoading.observe(this) {
            if (it == "false") {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        binding.mont.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                fetchEarnings("yearly")
            } else {
                fetchEarnings("monthly")
            }
        }

        fetchEarnings("monthly")
        observerEarnings()
    }

    private fun fetchEarnings(type: String) {
        viewModel.getAllEarnings(UserInfoPreference(this).getStr("token").toString(), type, 1)
    }

    private fun observerEarnings() {
        viewModel.observeEarning().observe(this@EarningsList, Observer { resource ->
            resource?.let {
                if (it.data?.data != null) {
                    earningList.clear()
                    monthList.clear()
                    earningList.addAll(it.data.data.list)
                    var cont =0
                    for (i in earningList) {
                        if (binding.mont.isChecked) {
                            cont=1
                            monthList.add(i.year.toString())
                        } else {
                            cont =0
                            monthList.add(i.month.toString())
                        }
                        finalEarning = it.data.data.Total_Earnings_all_Together
                    }
                    adapter = EarningListAdapter(monthList, earningList, finalEarning,cont)
                    binding.earnRcv.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                // Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
