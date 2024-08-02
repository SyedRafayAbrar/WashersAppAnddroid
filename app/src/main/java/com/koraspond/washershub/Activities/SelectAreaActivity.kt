package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ReasAdapter
import com.koraspond.washershub.Models.getCitieis.Data
import com.koraspond.washershub.Models.getCitieis.GetCities
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.ActivitySelectAreaBinding
import com.pegasus.pakbiz.network.Api
import retrofit2.Call
import retrofit2.Response

class SelectAreaActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectAreaBinding
    lateinit var list: ArrayList<Data>
    lateinit var adapter: ReasAdapter
    private var isNearby: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_area)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_area)

        binding.areaRcv.layoutManager = LinearLayoutManager(this)
        list = ArrayList()
        adapter = ReasAdapter(this@SelectAreaActivity,list) { areaId,areaname ->
            // Handle area selection
            onAreaSelected(areaId,areaname)
        }
        binding.areaRcv.adapter = adapter

        // Check if 'Nearby' was selected
        isNearby = intent.getBooleanExtra("isNearby", true)


            // Handle 'Nearby' selection
            binding.gnb.setOnClickListener {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("selectedArea", "Nearby")
                })
                finish()
            }


            // Fetch and display cities
            getCities()

    }

    private fun getCities() {
        val progress = ProgressDialog(this)
        progress.show()


        Api.client.getAllCities("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe", 1).enqueue(object : retrofit2.Callback<GetCities> {
            override fun onResponse(call: Call<GetCities>, response: Response<GetCities>) {
                progress.dismiss()
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        list.clear()
                        list.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@SelectAreaActivity, "Failed to load cities", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetCities>, t: Throwable) {
                Toast.makeText(this@SelectAreaActivity, t.message, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        })
    }

    private fun onAreaSelected(areaId: String,name:String) {
        val intent = Intent().apply {
            putExtra("selectedArea", areaId)
            putExtra("name", name)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}
