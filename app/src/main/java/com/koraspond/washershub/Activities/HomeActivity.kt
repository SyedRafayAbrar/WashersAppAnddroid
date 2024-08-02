package com.koraspond.washershub.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.koraspond.washershub.Adapters.HomeMenuAdapter
import com.koraspond.washershub.Models.getVendorsModel.Item
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.Utils.clickInterfaceVendor
import com.koraspond.washershub.ViewModel.VendorViewModel
import com.koraspond.washershub.ViewModel.VendorViewModelFactory
import com.koraspond.washershub.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), clickInterfaceVendor, NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: VendorViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 1
    lateinit var list: ArrayList<Item>
    var currentPage = 1
    var isLastPage = false
    var isLoading = false
    lateinit var adapter: HomeMenuAdapter
    var latitude = 0.0
    var longitude = 0.0
    var selectedAreaId: String? = null // Variable to store the selected area ID
    var isNearby = true // Initially set to nearby

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        list = ArrayList()
        binding.areaSpinner.text = "Nearest Shops"


        val headerView = binding.nav.getHeaderView(0)
        val navHeaderName = headerView.findViewById<TextView>(R.id.name)
        val navHeaderEmail = headerView.findViewById<TextView>(R.id.email)

        // Replace with actual user data
        navHeaderName.text = UserInfoPreference(this@HomeActivity).getStr("name")
        navHeaderEmail.text = UserInfoPreference(this@HomeActivity).getStr("email")

        val customerRepos = CustomerRepos.getInstance()
        viewModel = ViewModelProvider(this, VendorViewModelFactory(customerRepos))
            .get(VendorViewModel::class.java)
        val progress = ProgressDialog(this)

        viewModel.isLoading.observe(this) {
            if (it == "false") {
                progress.dismiss()
                isLoading = false
            } else {
                progress.show()
                isLoading = true
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

        setSortSpinner()
        setupScrollListener()

        binding.areaSpinner.setOnClickListener {
            startActivityForResult(Intent(this, SelectAreaActivity::class.java), REQUEST_CODE_SELECT_AREA)
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    adapter = HomeMenuAdapter(this, list, this, latitude, longitude)
                    binding.catRcv.adapter = adapter
                    currentPage = 1
                    isLastPage = false
                    list.clear()
                    adapter.notifyDataSetChanged()
                    fetchVendors()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchVendors() {
        if (isLoading || isLastPage) return
        isLoading = true
        val token = UserInfoPreference(this).getStr("token").toString()

        if (isNearby) {
            // Fetch nearby vendors using latitude and longitude
            viewModel.getNearbyVendors(token,1,1,currentPage, latitude, longitude).observe(this, Observer { resource ->
                resource?.let {
                    if (it.data != null && it.data.data.items != null) {
                        list.addAll(it.data.data.items)
                        adapter.notifyDataSetChanged()
                        currentPage++
                        isLastPage = currentPage > it.data.data.total_pages
                    } else {
                        Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            })
        } else {
            // Fetch vendors by area
            viewModel.getVendors(token, selectedAreaId!!.toInt(),1, currentPage).observe(this, Observer { resource ->
                resource?.let {
                    if (it.data != null && it.data.data.items != null) {
                        list.addAll(it.data.data.items)
                        adapter.notifyDataSetChanged()
                        currentPage++
                        isLastPage = currentPage > it.data.data.total_pages
                    } else {
                        Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            })
        }
    }

    private fun setupScrollListener() {
        binding.catRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        fetchVendors()
                    }
                }
            }
        })
    }

    fun setSortSpinner() {
        val sort: ArrayList<String> = ArrayList<String>()
        sort.add("Sort")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriesSpinner.adapter = arrayAdapter
    }

    fun setAreaSpinner() {
        val areas: ArrayList<String> = ArrayList<String>()
        areas.add("Area")
        areas.add("Malir")
        areas.add("Gulshan")
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // binding.areaSpinner.adapter = arrayAdapter

        binding.nav.setNavigationItemSelectedListener(this)
        binding.nav.menu.findItem(R.id.home).isChecked = true
    }

    override fun onClick(pos: Int, distance: Double) {
        val intent = Intent(this@HomeActivity, VendorDetailsActivity::class.java)
        intent.putExtra("id", list[pos].id)
        intent.putExtra("dist", distance)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                val intent = Intent(this@HomeActivity, HistoryActivity::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }
            R.id.logout -> {
                val intent = Intent(this@HomeActivity, WelcomeActivity::class.java)
                startActivity(intent)
                UserInfoPreference(this@HomeActivity).removeAllPrefData()
                finish()
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }
        }
        return false
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_AREA = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_AREA && resultCode == RESULT_OK) {
            val selectedArea = data?.getStringExtra("selectedArea")
            val selectedName=data?.getStringExtra("name")
            if (selectedArea == "Nearby") {
                isNearby = true
                selectedAreaId = null
                binding.areaSpinner.text = "Nearest Shops"
            } else {
                isNearby = false
                selectedAreaId = selectedArea
                binding.areaSpinner.text = selectedName
            }
            currentPage = 1
            isLastPage = false
            list.clear()
            fetchVendors()
        }
    }
}

