package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import com.koraspond.washershub.Adapters.EarningListAdapter
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Resource
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityVendorOrderDetailsBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorORderDetails : AppCompatActivity() {

    lateinit var binding:ActivityVendorOrderDetailsBinding
    lateinit var viewModel: VendoApisViewModel
    var id :String = ""
    var cancel:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_order_details)
        binding = DataBindingUtil.setContentView(this@VendorORderDetails,R.layout.activity_vendor_order_details)

        binding.backBtn.setOnClickListener {
            finish()
        }

        val customerRepos = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(customerRepos))
            .get(VendoApisViewModel::class.java)
        var progress = ProgressDialog(this@VendorORderDetails)
        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }
        fetchHistoryDetail(intent.getStringExtra("id").toString())
        setUnderline(binding.callNum,"Tap to Call Vendor")
        setUnderline(binding.orderTitle,"Order Date :")
        setUnderline(binding.ordDate,"12/24/2024")
        setUnderline(binding.dayTitle,"Order Day :")
        setUnderline(binding.dayDate,"Monday")
        setUnderline(binding.timeTitle,"Order Time :")
        setUnderline(binding.timeDate,"02:00 P.m - 03:00 A.M")
        setUnderline(binding.serTitle,"Services Selected")

       // ObserveOrderStatus()

        val token = UserInfoPreference(this).getStr("token").toString()
        binding.confrimBtn.setOnClickListener {


            var progres = ProgressDialog(this@VendorORderDetails)
            progres.show()

            var jsonObject = JsonObject().apply {
                addProperty("status","confirmed")
                addProperty("order_id",id)
                addProperty("reason","confirm")



            }
            val body: RequestBody = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())





            Api.client.updateStatus("token "+ token,body).enqueue(object :
                Callback<UpdateStatusModel> {
                override fun onResponse(call: Call<UpdateStatusModel>, response: Response<UpdateStatusModel>) {
                    progres.dismiss()
                    if (response.isSuccessful) {
                        if(response.body()!!.status==200){
                            Toast.makeText(this@VendorORderDetails,"Order Confirmed",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this@VendorORderDetails,"Connectivity error",Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<UpdateStatusModel>, t: Throwable) {
                    progres.dismiss()
                    Toast.makeText(this@VendorORderDetails,t.message,Toast.LENGTH_SHORT).show()
                }
            })

          //  viewModel.getUpdateStatus(token,"confirm",id,"")

        }
        binding.cancelBtn.setOnClickListener {

            if(cancel.isEmpty()){
                showAddCancelReason()
            }
//             viewModel.getUpdateStatus(token,"cancelled",id,"")




        }


    }

    fun showAddCancelReason() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val view: View = layoutInflater.inflate(R.layout.cancel_order_dialof, null)
        val variationTv = view.findViewById<TextView>(R.id.variationEditText)
        val addBtn = view.findViewById<MaterialButton>(R.id.addButton)


        var alertDialog: AlertDialog? = null

        addBtn.setOnClickListener {
            // Handle "Yes" button click
            // You can perform any action here
            // For example, dismiss the dialog
            if (variationTv.text.toString().trim().isEmpty()) {
                variationTv.setError("Please enter variation")
            } else {
                cancel =variationTv.text.toString().trim()
                cancelORder()
            }

            if (alertDialog != null) {
                alertDialog!!.dismiss()
            }

        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

    }

    private fun cancelORder() {
        var progres = ProgressDialog(this@VendorORderDetails)
        progres.show()

        var jsonObject = JsonObject().apply {
            addProperty("status","cancelled")
            addProperty("order_id",id)
            addProperty("reason",cancel)



        }
        val body: RequestBody = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())





        Api.client.updateStatus("token "+ UserInfoPreference(this@VendorORderDetails).getStr("token"),body).enqueue(object :
            Callback<UpdateStatusModel> {
            override fun onResponse(call: Call<UpdateStatusModel>, response: Response<UpdateStatusModel>) {
                progres.dismiss()
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                        Toast.makeText(this@VendorORderDetails,"Order Cancelled",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@VendorORderDetails,"Connectivity error",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<UpdateStatusModel>, t: Throwable) {
                progres.dismiss()
                Toast.makeText(this@VendorORderDetails,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ObserveOrderStatus() {
        viewModel.observeupdateStatus().observe(this@VendorORderDetails, Observer { resource ->
            resource?.let {
                if (it.data?.status != null) {

                    if(it.data?.status==200){
                        Toast.makeText(this,"order updated",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                // Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchHistoryDetail(orderid:String) {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getOrderDetail(token,orderid).observe(this@VendorORderDetails, Observer { resource ->
            resource?.let {
                if (it.data != null) {

                  //  binding.status.text = it.data.data.latest_status.name
                    binding.orderNum.text = "Order No # "+ it.data.data.unique_identifier
                    id = it.data.data.unique_identifier
                 //   binding.location.text = it.data.data.vendor.address
                    binding.ordDate.text = it.data.data.order_date
                    binding.timeDate.text = it.data.data.order_time_value
                    binding.serType.text = it.data.data.service.service_name
                    binding.varTitle.text = it.data.data.variation.name
                    binding.totalAmt.text = it.data.data.total.toString()

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