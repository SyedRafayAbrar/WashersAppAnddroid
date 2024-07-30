package com.koraspond.washershub.Activities

import android.app.Activity
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koraspond.washershub.Adapters.Service_variations_Adapter
import com.koraspond.washershub.Adapters.VariationAdapter
import com.koraspond.washershub.Models.addService.AddService
import com.koraspond.washershub.Models.getVariation.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.databinding.ActivityCreateServiceBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response

class CreateService : AppCompatActivity() {
    private lateinit var binding: ActivityCreateServiceBinding
    private lateinit var serviceVariationsAdapter: Service_variations_Adapter
    private val selectedVariations = mutableListOf<Data>()

    private lateinit var viewModel: VendoApisViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var catList: ArrayList<com.koraspond.washershub.Models.getCategory.Data>
    lateinit var  catNames:ArrayList<String>
    var selectedid =-1
    companion object {
        private const val ADD_SERVICE_VARIATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_service)

        binding.varRcv.layoutManager = LinearLayoutManager(this)
        serviceVariationsAdapter = Service_variations_Adapter(selectedVariations)
        binding.varRcv.adapter = serviceVariationsAdapter
        val vendorRepo = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(vendorRepo))
            .get(VendoApisViewModel::class.java)
        catList = ArrayList()
        catNames= ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, catNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.catSpinner.adapter = adapter

        binding.catSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

               selectedid = catList.get(position).id

                //  Toast.makeText(this@VendorDetailsActivity, amount.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected if needed
            }
        }

        val progress = ProgressDialog(this@CreateService)
        viewModel.isLoading.observe(this) {
            if (it == "false") {
                progress.dismiss()
            } else {
                progress.show()
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.addVar.setOnClickListener {
            startActivityForResult(Intent(this, AddServiceVariation::class.java), ADD_SERVICE_VARIATION_REQUEST_CODE)
        }

        fetchCategories()


        setUnderline(binding.sel, binding.sel.text.toString())
        setUnderline(binding.addVar, binding.addVar.text.toString())
        setUnderline(binding.noteAdmin, binding.noteAdmin.text.toString())
    }

    private fun fetchCategories() {
        viewModel.getCat("").observe(this@CreateService, Observer { resource ->
            resource?.let {
                if (it.data?.data != null) {
                    catNames.clear()
                    catList.clear()
                    catList.addAll(it.data.data)
                    for(i in catList){
                        catNames.add(i.name)
                    }
                    selectedid = catList.get(0).id

                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })

        binding.creatBtn.setOnClickListener {
            createService()
        }
    }


    fun createService(){



        var progressDialog = ProgressDialog(this)



        if(selectedVariations.isEmpty()){
            Toast.makeText(this, "please select variation", Toast.LENGTH_SHORT).show()
        }
        else{
            if(binding.serviceName.text.toString().trim().isEmpty()){
                binding.serviceName.error = "Please enter service name"
            }
            else {
               if(binding.descTv.text.toString().trim().isEmpty()){
                   binding.descTv.error = "please enter service detail"
               }
                else{
                    if(binding.capTv.text.toString().trim().isEmpty()){
                        binding.capTv.error = "Please enter description"
                    }
                   else{
                       if(binding.durTv.text.toString().isEmpty()){
                           binding.durTv.error = "please enter duration"
                       }
                        else{
                           progressDialog.show()
                           val catarray=JsonArray()
                           catarray.add(selectedid)
                           // Create the JSON structure
                           val jsonArray = JsonArray()
                           for(i in selectedVariations) {
                               val arrayObj = JsonObject().apply {
                                   addProperty("variation_id", i.id)
                                   addProperty("amount", i.amount)
                                   addProperty("is_time_constraint",i.constraint)

                               }
                               jsonArray.add(arrayObj)
                           }

                           val jsonObject = JsonObject().apply {
                               addProperty("service_name", binding.serviceName.text.toString().trim())
                               addProperty("service_description", binding.descTv.text.toString().trim())
                               addProperty("service_charges", 0)
                               addProperty("service_approx_time",binding.durTv.text.toString().trim())
                               addProperty("vendor",UserInfoPreference(this@CreateService).getStr("vid"))
                               add("cat_ids",catarray)
                               addProperty("is_time_constraint",false)
                               add("service_variations",jsonArray)

                           }

                           val body: RequestBody = jsonObject.toString()
                               .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


                           val token = UserInfoPreference(this).getStr("token").toString()
                           Api.client.addService("token "+token,body).enqueue(object :retrofit2.Callback<AddService>{
                               override fun onResponse(call: Call<AddService>, response: Response<AddService>) {
                                   progressDialog.dismiss()
                                   if(response.isSuccessful){
                                       if(response.body()!=null && response.body()!!.data!=null){
                                           if(response.body()!!.status==200){
                                               Toast.makeText(this@CreateService,"service created",Toast.LENGTH_SHORT).show()
                                            finish()
                                           }

                                       }
                                   }
                                   else{
                                       Toast.makeText(this@CreateService, "Connectivity error", Toast.LENGTH_SHORT).show()
                                   }
                               }

                               override fun onFailure(call: Call<AddService>, t: Throwable) {
                                   progressDialog.dismiss()
                                   Toast.makeText(this@CreateService, t.message, Toast.LENGTH_SHORT).show()
                               }

                           })
                        }
                   }
                }
            }

        }


    }
    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_SERVICE_VARIATION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val variations = data?.getParcelableArrayListExtra<Data>("selectedVariations")
            variations?.let {
                selectedVariations.clear()
                selectedVariations.addAll(it)
                serviceVariationsAdapter.notifyDataSetChanged()
            }
        }
    }
}
