package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityHistoryDetailBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetail : AppCompatActivity() {

    lateinit var binding:ActivityHistoryDetailBinding
    lateinit var viewModel: VendorViewModel
   lateinit var data:com.koraspond.washershub.Models.orderDetailModel.Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
        binding = DataBindingUtil.setContentView(this@HistoryDetail,R.layout.activity_history_detail)

        val customerRepos = CustomerRepos.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(customerRepos))
            .get(VendorViewModel::class.java)
        var progress = ProgressDialog(this@HistoryDetail)
        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        var oid = intent.getStringExtra("oid").toString()
        setUnderline(binding.location,"Gulshan e iqbal karachi ")
        setUnderline(binding.callNum,"Tap to Call Vendor")
        setUnderline(binding.orderTitle,"Order Date :")
        setUnderline(binding.ordDate,"12/24/2024")
        setUnderline(binding.dayTitle,"Order Day :")
        setUnderline(binding.dayDate,"Monday")
        setUnderline(binding.timeTitle,"Order Time :")
        setUnderline(binding.timeDate,"02:00 P.m - 03:00 A.M")
        setUnderline(binding.serTitle,"Services Selected")


        if(UserInfoPreference(this).getStr("role").equals("v")){
            binding.materialButton.visibility = View.GONE
            setUnderline(binding.callNum,"Tap to Call User")
        }

        binding.materialButton.setOnClickListener{
            var intent = Intent(this@HistoryDetail,RateActivity::class.java)
            intent.putExtra("vendor",data.vendor.id.toString())
            intent.putExtra("user",UserInfoPreference(this).getStr("id"))
            intent.putExtra("order",oid)
            startActivity(intent)
        }
binding.backBtn.setOnClickListener{
    finish()
}

        binding.cancelBtn.setOnClickListener {
            cancelORder()
        }

    }
    private fun cancelORder() {
        var progres = ProgressDialog(this)
        progres.show()

        var jsonObject = JsonObject().apply {
            addProperty("status","cancelled")
            addProperty("order_id",data.unique_identifier)
            addProperty("reason",binding.cancelEt.text.toString())



        }
        val body: RequestBody = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())





        Api.client.updateStatus("token "+ UserInfoPreference(this).getStr("token"),body).enqueue(object :
            Callback<UpdateStatusModel> {
            override fun onResponse(call: Call<UpdateStatusModel>, response: Response<UpdateStatusModel>) {
                progres.dismiss()
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                        Toast.makeText(this@HistoryDetail,"Order Cancelled",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@HistoryDetail,"Connectivity error",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<UpdateStatusModel>, t: Throwable) {
                progres.dismiss()
                Toast.makeText(this@HistoryDetail,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        fetchHistoryDetail(intent.getStringExtra("id").toString())
    }
    private fun fetchHistoryDetail(orderid:String) {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getOrderDetail(token,orderid).observe(this@HistoryDetail, Observer { resource ->
            resource?.let {
                if (it.data != null) {
                    data = it.data.data
                        binding.status.text = it.data.data.latest_status.name
                        binding.orderNum.text = "Order No # "+ it.data.data.unique_identifier
                        binding.location.text = it.data.data.vendor.address
                        binding.ordDate.text = it.data.data.order_date
                        binding.timeDate.text = it.data.data.order_time_value
                        binding.serType.text = it.data.data.service.service_name
                    binding.varTitle.text = it.data.data.variation.name
                    binding.totalAmt.text = it.data.data.total.toString()
                    if(it.data.data.latest_status.name!="pending"){
                        binding.cancelBtn.visibility = View.GONE
                        binding.cancelCv.visibility = View.GONE
                    }

                    if(it.data.data.is_reviewed){
                        binding.materialButton.visibility = View.GONE
                    }

                } else {
                    Toast.makeText(this, "order detail not found", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }
}