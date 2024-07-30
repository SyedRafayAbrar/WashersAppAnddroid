package com.koraspond.washershub.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ServiceSelectionAdapter
import com.koraspond.washershub.Adapters.TimeSelectionAdapter
import com.koraspond.washershub.Models.CreqteORderRequest
import com.koraspond.washershub.Models.vendorDetails.Service
import com.koraspond.washershub.Models.vendorDetails.Variation
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.Utils.clickInterface
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityVendorDetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VendorDetailsActivity : AppCompatActivity(), clickInterface {

    lateinit var binding: ActivityVendorDetailsBinding
    var id = 0
    var distance = 0.0
    lateinit var viewModel: VendorViewModel
    lateinit var servList: ArrayList<Service>
    lateinit var variations: ArrayList<Variation>
    lateinit var variationsNames: ArrayList<String>
    lateinit var variationsAdapter: ArrayAdapter<String>
    var timeSelectionAdapter: TimeSelectionAdapter? = null
    var variationid = 0
    var amount = 0.0
    var servpos = -1


    lateinit var timeslots: ArrayList<String>

    lateinit var timeSlotAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vendor_details)
        val currentDate = getCurrentDate()
        servList = ArrayList()
        variations = ArrayList()
        variationsNames = ArrayList()
        variationsAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variationsNames)

        timeslots = ArrayList()
        timeSlotAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeslots)
        id = intent.getIntExtra("id", 0)
        distance = intent.getDoubleExtra("dist", 0.0)
        val customerRepos = CustomerRepos.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(customerRepos))
            .get(VendorViewModel::class.java)

        servList = ArrayList()
        val progress = ProgressDialog(this)

        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        fetchVendorDetails()


        binding.materialButton.setOnClickListener {
            var intent = Intent(this@VendorDetailsActivity, ReviewsViewActivity::class.java)
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.dateTv.text = currentDate
        binding.dateTv.setOnClickListener {
            showDatePicker()
        }
        setUnderline(binding.location, "Gulshan e iqbal karachi")
        setUnderline(binding.sd, "Select Date")
        setUnderline(binding.td, "Select Time")
        setUnderline(binding.so, "Services Offered")
        setUnderline(binding.slo, "Select options")

        setVehicleSpinner()

        binding.timeRcv.layoutManager =
            GridLayoutManager(this, 4, GridLayoutManager.HORIZONTAL, false)
        timeSelectionAdapter = TimeSelectionAdapter(this, timeslots)
        binding.timeRcv.adapter = timeSelectionAdapter



        binding.servicesRcv.layoutManager = LinearLayoutManager(this)


        binding.crBtn.setOnClickListener {
            if (servList.isEmpty() || servpos == -1) {
                Toast.makeText(this, "Please select service first", Toast.LENGTH_SHORT).show()
            } else {
                if (timeslots.isEmpty()) {
                    Toast.makeText(this, "No Time slot available", Toast.LENGTH_SHORT).show()
                } else {
                    if (timeSelectionAdapter != null) {
                        if (timeSelectionAdapter!!.getSelectedTimeSlot().isNullOrEmpty()) {
                            Toast.makeText(
                                this@VendorDetailsActivity,
                                "Please select a timeslot",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            var request = CreqteORderRequest(
                                servList.get(servpos).id,
                                binding.dateTv.text.toString(),
                                timeSelectionAdapter!!.getSelectedTimeSlot().toString(),
                                servList.get(servpos).vendor.end_time.toString(),
                                1,
                                id,
                                amount,
                                servList.get(servpos).is_time_constraint,
                                variationid
                            )
                            showConfirmationDialog(request)
                            //viewModel.createORder(UserInfoPreference(this@VendorDetailsActivity).getStr("token").toString(),request)
                        }
                    }
                }
            }
        }


    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = content
    }

    private fun fetchVendorDetails() {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getVendorDetail(token, id).observe(this, Observer { resource ->
            resource?.let {
                if (it.data != null) {
                    binding.shopName.text = it.data.data.shop_name
                    binding.location.text = it.data.data.address
                    binding.time.text = it.data.data.start_time + " - " + it.data.data.end_time
                    binding.distance.text = distance.toString()
                    binding.ratBar.rating = (it.data.data.average_rating ?: "0.0").toFloat()
                    servList.clear()
                    servList.addAll(it.data.data.services as ArrayList)

                    var adapterSer = ServiceSelectionAdapter(this, servList, this)
                    binding.servicesRcv.adapter = adapterSer
                    adapterSer.notifyDataSetChanged()


                } else {
                    Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchTimeSlots(serviceid: Int) {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getTimeSlots(token, id, serviceid).observe(this, Observer { resource ->
            resource?.let {
                if (it.data != null) {

                    timeslots.addAll(it.data.data)
                    if (!timeslots.isEmpty()) {
                        binding.td.visibility = View.VISIBLE
                        binding.timeRcv.visibility = View.VISIBLE
                    } else {
                        binding.td.visibility = View.GONE
                        binding.timeRcv.visibility = View.GONE
                    }


                    timeSlotAdapter.notifyDataSetChanged()


                } else {
                    //Toast.makeText(this, "no time slot found", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                //Toast.makeText(this, "no time slot found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showConfirmationDialog(request: CreqteORderRequest) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Order")
        builder.setMessage("Are you sure you want to place this order?")
        builder.setPositiveButton("Yes") { dialog, which ->
            createorder(request)
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun createorder(req: CreqteORderRequest) {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.createORder(token, req).observe(this, Observer { resource ->
            resource?.let {
                if (it.data != null) {
                    if (it.data.status == 200) {
                        Toast.makeText(this, "order created succesfuly", Toast.LENGTH_SHORT).show()
                        // startActivity(Intent(this@VendorDetailsActivity,HistoryActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "unable to place order", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker?, year: Int, month: Int, day: Int ->
                // Format the selected date to "yyyy-MM-dd"
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.dateTv.text = dateFormat.format(selectedDate.time)
            }, year, month, dayOfMonth
        )

        datePickerDialog.show()
    }

    fun setVehicleSpinner() {


        variationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehSpinner.adapter = variationsAdapter
        binding.vehSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                amount = variations[position].amount
                variationid = variations.get(position).id
                binding.amount.text = amount.toString()

                //  Toast.makeText(this@VendorDetailsActivity, amount.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected if needed
            }
        }

    }

    override fun onClick(pos: Int) {
        servpos = pos
        variationsNames.clear()
        variations.clear()
        timeslots.clear()
        fetchTimeSlots(servList.get(pos).id)
        if (!servList.get(pos).variations.isNullOrEmpty()) {
            binding.slo.visibility = View.VISIBLE
            binding.selectCv.visibility = View.VISIBLE

            variations.addAll(servList.get(pos).variations as ArrayList)
            for (i in variations) {
                variationsNames.add(i.variation.name)

            }
            amount = variations.get(0).amount
            variationid = variations.get(0).id
            binding.amount.text = amount.toString()
            variationsAdapter.notifyDataSetChanged()


        } else {
            binding.slo.visibility = View.VISIBLE
            binding.selectCv.visibility = View.VISIBLE
        }
    }
}