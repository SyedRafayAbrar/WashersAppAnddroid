package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Adapters.HistoryAdapter
import com.koraspond.washershub.Models.orderHistory.Item
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityHistoryBinding
    lateinit var viewModel: VendorViewModel
    lateinit var ordrlist: ArrayList<Item>
    lateinit var adapter: HistoryAdapter
    var currentPage = 1
    var isLastPage = false
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        val customerRepos = CustomerRepos.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(customerRepos))
            .get(VendorViewModel::class.java)
        ordrlist = ArrayList()
        adapter = HistoryAdapter(1, ordrlist)
        binding.histRcv.layoutManager = LinearLayoutManager(this)
        binding.histRcv.adapter = adapter

        val progress = ProgressDialog(this)
        viewModel.isLoading.observe(this) {
            if (it == "false") {
                progress.dismiss()
                isLoading = false
            } else {
                progress.show()
                isLoading = true
            }
        }

        binding.back.setOnClickListener {
            finish()
        }
        setSortSpinner()
        setupScrollListener()

    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        isLastPage = false
        ordrlist.clear()
        adapter.notifyDataSetChanged()
        fetchHistory()
    }

    private fun fetchHistory() {
        if (isLoading || isLastPage) return
        isLoading = true

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getHistory(token, currentPage).observe(this, Observer { resource ->
            resource?.let {
                if (it.data != null) {
                    ordrlist.addAll(it.data.data.items)
                    adapter.notifyDataSetChanged()
                    currentPage++
                    isLastPage = currentPage > it.data.data.total_pages
                } else {
                    Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        })
    }

    private fun setupScrollListener() {
        binding.histRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        ) {
                        Log.d("dskmfdsmfdss", currentPage.toString())
                        fetchHistory()
                    }
                }
            }
        })
    }

    private fun setSortSpinner() {
        val areas = arrayListOf("Ascending", "Descending")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = arrayAdapter
    }
}
