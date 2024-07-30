package com.koraspond.washershub.Activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.koraspond.washershub.Adapters.VariationAdapter
import com.koraspond.washershub.Models.getVariation.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.databinding.ActivityAddServiceVariationBinding
import com.koraspond.washershub.databinding.AddVarDialogBinding

class AddServiceVariation : AppCompatActivity(), VariationAdapter.OnItemClickListener {

    private lateinit var binding: ActivityAddServiceVariationBinding
    private lateinit var viewModel: VendoApisViewModel
    private lateinit var adapter: VariationAdapter
    private lateinit var variationsList: ArrayList<Data>
    private val selectedVariations = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_variation)

        binding = DataBindingUtil.setContentView(
            this@AddServiceVariation,
            R.layout.activity_add_service_variation
        )

        val vendorRepo = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(vendorRepo))
            .get(VendoApisViewModel::class.java)
        variationsList = ArrayList()
        adapter = VariationAdapter(variationsList, this)
        binding.varRcv.layoutManager = LinearLayoutManager(this@AddServiceVariation)
        binding.varRcv.adapter = adapter

        val progress = ProgressDialog(this@AddServiceVariation)
        viewModel.isLoading.observe(this) {
            if (it == "false") {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        binding.addBtn.setOnClickListener {
            showAddVariationDialog()
        }

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getVariations(token)
        fetchAllVariations()
        observeAddVariation()

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.saveBtn.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra("selectedVariations", ArrayList(selectedVariations))
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onItemClicked(selectedVariations: List<Data>) {
        this.selectedVariations.clear()
        this.selectedVariations.addAll(selectedVariations)
    }

    private fun observeAddVariation() {
        viewModel.getAddVariation().observe(this, Observer {
            if (it != null && it.data?.data != null) {
                Toast.makeText(this@AddServiceVariation, "Service added", Toast.LENGTH_SHORT).show()
                viewModel.getVariations(UserInfoPreference(this).getStr("token").toString())
            }
        })
    }

    fun showAddVariationDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val view: View = layoutInflater.inflate(R.layout.add_var_dialog, null)
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
                addVariation(variationTv.text.toString().trim())
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
//    private fun showAddVariationDialog() {
//        val dialogBinding: AddVarDialogBinding = DataBindingUtil.inflate(layoutInflater, R.layout.add_var_dialog, null, false)
//        val dialog = Dialog(this, R.style.CustomDialog)
//        dialog.setContentView(dialogBinding.root)
//
//        dialog.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//
//        val layoutParams = dialog.window?.attributes
//        val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
//        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
//        layoutParams?.horizontalMargin = margin / resources.displayMetrics.widthPixels.toFloat()
//        dialog.window?.attributes = layoutParams
//
//        dialogBinding.addButton.setOnClickListener {
//            val variation = dialogBinding.variationEditText.text.toString().trim()
//            if (variation.isNotEmpty()) {
//                addVariation(variation)
//                dialog.dismiss()
//            } else {
//                Toast.makeText(this, "Please enter a variation", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        dialog.show()
//    }


    private fun addVariation(variation: String) {
        val token = UserInfoPreference(this).getStr("token").toString()
        val id = UserInfoPreference(this).getStr("vid").toString()
        viewModel.addVariation(token, variation, id)
    }

    private fun fetchAllVariations() {
        viewModel.observeVariation().observe(this@AddServiceVariation, Observer { resource ->
            resource?.let {
                if (it.data?.data != null) {
                    variationsList.clear()
                    variationsList.addAll(it.data.data)
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
