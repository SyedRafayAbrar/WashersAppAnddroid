package com.koraspond.washershub.Activities


import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.koraspond.washershub.Models.getCitieis.Data
import com.koraspond.washershub.Models.getCitieis.GetCities
import com.koraspond.washershub.Models.vendSignup.VendorSignup
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.ActivitySignupBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.util.*

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var cities: ArrayList<Data>
    lateinit var citiesNames: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    var selAreaId = -1
    var latitude =0.0
    var longitude =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Initialize views
        initializeViews()

        // Fetch cities data
        fetchCities()
    }

    private fun initializeViews() {

        binding.signup.setOnClickListener {
            vendorSignu()
        }
        binding.shopOpenTime.setOnClickListener {
            showTimePicker(binding.shopOpenTime)
        }

        binding.shopCloseTime.setOnClickListener {
            showTimePicker(binding.shopCloseTime)
        }

        binding.location.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, REQUEST_LOCATION)
        }

        cities = ArrayList()
        citiesNames = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, citiesNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areaSpinner.adapter = adapter

        binding.areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selAreaId = cities[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected if needed
            }
        }
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                editText.setText(time)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    fun vendorSignu(){
        if(binding.nameEt.text.toString().trim().isEmpty()){
          binding.nameEt.setError("please enter user name")
        }
        else{
            if(binding.shopNameEt.text.toString().trim().isEmpty()){
                binding.shopNameEt.setError("Please enter shop name")
            }
            else{
                if(binding.emailEt.text.toString().trim().isEmpty()){
                    binding.emailEt.setError("Please enter email")

                }
                else{
                    if(binding.addressEt.text.toString().trim().isEmpty()){
                        binding.addressEt.setError("please enter shop address")
                    }
                    else{
                        if(binding.contactNo.text.toString().trim().isEmpty()){
                            binding.contactNo.setError("Please enter contact number")
                        }
                        else{
                            if(binding.nPassword.text.toString().isEmpty()){
                                binding.nPassword.setError("Please enter password")
                            }
                            else{
                                if(binding.cPassword.text.toString().trim().isEmpty()){
                                    binding.cPassword.setError("Please fill this field")
                                }
                                else{
                                    if(binding.location.text.toString().isEmpty()){
                                        binding.location.setError("Please select shop location")
                                    }
                                    else{
                                        if(binding.shopOpenTime.text.toString().isEmpty()){
                                            binding.shopOpenTime.setError("please select shop open time")
                                        }
                                        else{
                                            if(binding.shopCloseTime.text.toString().isEmpty()){
                                                binding.shopCloseTime.setError("Please select shop close time")
                                            }
                                            else{
                                                if(binding.nPassword.text.toString().trim().equals(binding.cPassword.text.toString().trim())){
                                                   var progress = ProgressDialog(this)
                                                    progress.show()
                                                    var jsonObject = JsonObject().apply {
                                                        addProperty("username",binding.nameEt.text.toString().trim())
                                                        addProperty("email",binding.emailEt.text.toString())
                                                        addProperty("password",binding.cPassword.text.toString())
                                                        addProperty("contact_number",binding.contactNo.text.toString().trim())
                                                        addProperty("role","vendor")
                                                        addProperty("shop_name",binding.shopNameEt.text.toString())
                                                        addProperty("lat",latitude.toString())
                                                        addProperty("long",longitude.toString())
                                                        addProperty("address",binding.addressEt.text.toString())
                                                        addProperty("vendor_capacity",5)
                                                        addProperty("start_time",binding.shopOpenTime.text.toString())

                                                        addProperty("end_time",binding.shopCloseTime.text.toString())
                                                        addProperty("timeslots_duration",60)
                                                        addProperty("vendor_category",1)
                                                        addProperty("area",selAreaId)


                                                    }
                                                    val body: RequestBody = jsonObject.toString()
                                                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                                                    Api.client.vendorSignup(body,"Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe").enqueue(object :retrofit2.Callback<VendorSignup>{
                                                        override fun onResponse(
                                                            call: Call<VendorSignup>,
                                                            response: Response<VendorSignup>
                                                        ) {
                                                            progress.dismiss()
                                                            if(response.isSuccessful){
                                                                if(response.body()!!.status==200 && response.body()!!.data!=null){
                                                                    var userInfoPreference = UserInfoPreference(this@SignupActivity)
                                                                    userInfoPreference.setStr("isLogin","true")
                                                                    userInfoPreference.setStr("vid",response.body()!!.data!!.vendor_details.user.toString())
                                                                    userInfoPreference.setStr("name",response.body()!!.data!!.user_name)
                                                                    userInfoPreference.setStr("email",response.body()!!.data!!.email)
                                                                    userInfoPreference.setStr("token",response.body()!!.data!!.token)
                                                                    userInfoPreference.setStr("role","c")
                                                                    var  intent = Intent(this@SignupActivity,VendorHome::class.java)
                                                                    startActivity(intent)
                                                                  finish()
                                                                    Toast.makeText(this@SignupActivity,"vendor signup success",Toast.LENGTH_SHORT).show()

                                                                }
                                                                else{
                                                                    Toast.makeText(this@SignupActivity,"unable to signup"+response.code(),Toast.LENGTH_SHORT).show()

                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(this@SignupActivity,"unable to signup"+response.code(),Toast.LENGTH_SHORT).show()
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<VendorSignup>,
                                                            t: Throwable
                                                        ) {
                                                            progress.dismiss()
                                                           Toast.makeText(this@SignupActivity,t.message,Toast.LENGTH_SHORT).show()
                                                        }

                                                    })
                                                }
                                                else{
                                                    Toast.makeText(this@SignupActivity,"Password are not same",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun fetchCities() {
        val progressDialog = ProgressDialog(this)
        progressDialog.show()

        Api.client.getCities("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe",1).enqueue(object : retrofit2.Callback<GetCities> {
            override fun onResponse(call: Call<GetCities>, response: Response<GetCities>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    response.body()?.data?.let { citiesList ->
                        cities.clear()
                        citiesNames.clear()
                        cities.addAll(citiesList)
                        citiesNames.addAll(citiesList.map { it.name })
                        if (cities.isNotEmpty()) {
                            selAreaId = cities[0].id
                        }
                        adapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(this@SignupActivity, "No Area Found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Connectivity error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetCities>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            data?.let {
                 latitude = it.getDoubleExtra("latitude", 0.0)
                 longitude = it.getDoubleExtra("longitude", 0.0)
                val address = it.getStringExtra("address")
                binding.location.setText(address)

                // You can also store latitude and longitude for further use
            }
        }
    }
    companion object {
        private const val REQUEST_LOCATION = 1
    }
}
