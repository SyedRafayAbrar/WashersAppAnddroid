package com.koraspond.washershub.Activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.koraspond.washershub.Adapters.HomeMenuAdapter
import com.koraspond.washershub.Models.getVendorsModel.Data
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepo
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.Utils.clickInterface
import com.koraspond.washershub.Utils.clickInterfaceVendor
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), clickInterfaceVendor, NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: VendorViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 1
    lateinit var list : ArrayList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        list = ArrayList()

        val vendorRepo = VendorRepo.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(vendorRepo))
            .get(VendorViewModel::class.java)
        val progress = ProgressDialog(this)

        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        binding.menu.setOnClickListener {
            if (!binding.myDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.myDrawerLayout.openDrawer(Gravity.LEFT)
            } else {
                binding.myDrawerLayout.closeDrawer(Gravity.RIGHT)
            }
        }

        binding.catRcv.layoutManager = LinearLayoutManager(this)
        binding.filter.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CatogariesActivity::class.java))
        }

        setAreaSpinner()
        setSortSpinner()
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    fetchVendors(latitude, longitude)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchVendors(latitude: Double, longitude: Double) {

        val token = UserInfoPreference(this).getStr("token").toString()
        viewModel.getVendors(token, 1, 1).observe(this, Observer { resource ->
            resource?.let {
                if (it.data != null) {
                    list.clear()
                    list.addAll(it.data.data)
                    val adapter = HomeMenuAdapter(this, it.data.data as ArrayList<Data>, this, latitude, longitude)
                    binding.catRcv.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setSortSpinner() {
        val sort: ArrayList<String> = ArrayList<String>()
        sort.add("Sort")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriesSpinner.setAdapter(arrayAdapter)
    }

    fun setAreaSpinner() {
        val areas: ArrayList<String> = ArrayList<String>()
        areas.add("Area")
        areas.add("Malir")
        areas.add("Gulshan")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areaSpinner.setAdapter(arrayAdapter)

        binding.nav.setNavigationItemSelectedListener(this)
        binding.nav.menu.findItem(R.id.home).isChecked = true
    }

    override fun onClick(pos: Int,distance:Double) {
        val intent = Intent(this@HomeActivity, VendorDetailsActivity::class.java)
        intent.putExtra("id",list.get(pos).id)
        intent.putExtra("dist",distance)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                val intent = Intent(this@HomeActivity, HistoryActivity::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
