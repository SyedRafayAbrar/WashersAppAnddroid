package com.koraspond.washershub.Fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ServiceSelectionAdapter
import com.koraspond.washershub.Adapters.TimeSelectionAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.FragmentCatDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CatDetailFragment : Fragment() {

    lateinit var binding:FragmentCatDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.setContentView(requireActivity(),R.layout.fragment_cat_detail)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cat_detail, container, false)
    }
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    private fun setUnderline(textView: TextView, text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        content.setSpan(ForegroundColorSpan(Color.BLACK), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = content
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _: DatePicker?, year: Int, month: Int, day: Int ->
                // Handle the selected date
                // You can set the selected date to your TextView or do any other actions here
                binding.dateTv.text = "$day-${month + 1}-$year"
            }, year, month, dayOfMonth)

        datePickerDialog.show()
    }
    fun setVehicleSpinner(){
        var areas:ArrayList<String> = ArrayList<String>()
        areas.add("Car")
        areas.add("Bike")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehSpinner.setAdapter(arrayAdapter)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            requireActivity().supportFragmentManager.popBackStack(
//            )
//        }
        val currentDate = getCurrentDate()
        binding.dateTv.text = currentDate
        binding.dateTv.setOnClickListener {
            showDatePicker()
        }
        setUnderline(binding.location,"Gulshan e iqbal karachi")
        setUnderline(binding.sd,"Select Date")
        setUnderline(binding.td,"Select Time")
        setUnderline(binding.so,"Services Offered")
        setUnderline(binding.slo,"Select options")

        setVehicleSpinner()

        binding.timeRcv.layoutManager = GridLayoutManager(requireContext(),3)
        //binding.timeRcv.adapter = TimeSelectionAdapter(requireContext())


        binding.servicesRcv.layoutManager = LinearLayoutManager(requireContext())
      //  binding.servicesRcv.adapter = ServiceSelectionAdapter(requireContext())


    }

}