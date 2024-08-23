package com.koraspond.washershub.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ServiceSelectionAdapter
import com.koraspond.washershub.Adapters.TimeSelectionAdapter
import com.koraspond.washershub.Models.CreqteORderRequest
import com.koraspond.washershub.Models.vendorDetails.Field
import com.koraspond.washershub.Models.vendorDetails.Service
import com.koraspond.washershub.Models.vendorDetails.Variation
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.Utils.clickInterface
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityVendorDetailsBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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
        variationsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, variationsNames)

        timeslots = ArrayList()
        timeSlotAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeslots)
        id = intent.getIntExtra("id", 0)
        distance = intent.getDoubleExtra("dist", 0.0)
        val customerRepos = CustomerRepos.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(customerRepos)).get(VendorViewModel::class.java)

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
            val intent = Intent(this@VendorDetailsActivity, ReviewsViewActivity::class.java)
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

        binding.timeRcv.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.HORIZONTAL, false)
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
                            val (fields, isValid) = collectDynamicFields()
                            if (isValid) {
                                val request = CreqteORderRequest(
                                    servList[servpos].id,
                                    binding.dateTv.text.toString(),
                                    timeSelectionAdapter!!.getSelectedTimeSlot().toString(),
                                    servList[servpos].vendor.end_time.toString(),
                                    1,
                                    id,
                                    amount,
                                    servList[servpos].is_time_constraint,
                                    variationid,
                                    fields
                                )
                                showConfirmationDialog(request)
                            } else {
                                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
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
                    binding.ratBar.rating = ((it.data.data.average_rating ?: 0.0).toFloat())
                    servList.clear()
                    servList.addAll(it.data.data.services as ArrayList)

                    for (field in it.data.data.fields) {
                        addDynamicField(field)
                    }
                    val adapterSer = ServiceSelectionAdapter(this, servList, this)
                    binding.servicesRcv.adapter = adapterSer
                    adapterSer.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Throws(JSONException::class)
    private fun addDynamicField(fieldObject: Field) {
        val name = fieldObject.name
        val placeholder = fieldObject.placeholder
        val validation = fieldObject.validation
        val type = fieldObject.type
        val editText = EditText(this)
        editText.hint = placeholder
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        layoutParams.setMargins(14, 6, 14, 0) // Setting marginStart, marginTop, marginEnd, marginBottom
        editText.layoutParams = layoutParams
        if ("number".equals(type, ignoreCase = true)) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }
        editText.hint = name
        if (validation != null && validation.isNotEmpty()) {
            editText.filters = arrayOf<InputFilter>(
                object : InputFilter {
                    var pattern: Pattern? = Pattern.compile(validation)
                    override fun filter(
                        source: CharSequence,
                        start: Int,
                        end: Int,
                        dest: Spanned,
                        dstart: Int,
                        dend: Int
                    ): String? {
                        val matcher: Matcher = pattern!!.matcher(source)
                        return if (!matcher.matches()) {
                            ""
                        } else null
                    }
                }
            )
        }
        binding.dynamic.addView(editText)
        editText.tag = fieldObject.id // Assign field ID to the EditText's tag for later retrieval
    }

    private fun collectDynamicFields(): Pair<List<Map<String, Any>>, Boolean> {
        val fields = mutableListOf<Map<String, Any>>()
        var isValid = true

        for (i in 0 until binding.dynamic.childCount) {
            val view = binding.dynamic.getChildAt(i)
            if (view is EditText) {
                val fieldId = view.tag as? Int ?: continue
                val fieldValue = view.text.toString()

                if (fieldValue.isEmpty()) {
                    isValid = false
                    break
                }

                fields.add(mapOf("id" to fieldId, "value" to fieldValue))
            }
        }
        return Pair(fields, isValid)
    }

    private fun showConfirmationDialog(request: CreqteORderRequest) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure to create order?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            createOrder(request)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun createOrder(request: CreqteORderRequest) {
        val progress = ProgressDialog(this)
        progress.setMessage("Loading")
        progress.show()

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.createORder(token, request).observe(this, Observer { resource ->
            progress.dismiss()
            resource?.let {
                if (it.data != null) {
                    Toast.makeText(this, "Order created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to create order", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.dateTv.text = selectedDate
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun setVehicleSpinner() {
        val vehicles = arrayListOf("Toyota", "Suzuki", "Honda", "Nissan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehSpinner.adapter = adapter
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
