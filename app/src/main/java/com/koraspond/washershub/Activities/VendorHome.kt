package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.koraspond.washershub.Adapters.VendorRecentOrderAdapter
import com.koraspond.washershub.ProfileActivity
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Utils.StaticClass
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.VendorServicesList
import com.koraspond.washershub.ViewModel.VendoApisViewModel
import com.koraspond.washershub.ViewModel.VendorApisViewModelFactory
import com.koraspond.washershub.databinding.ActivityVendorHomeBinding
import de.hdodenhof.circleimageview.CircleImageView

class VendorHome : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityVendorHomeBinding
    lateinit var viewModel: VendoApisViewModel
    lateinit var adapter: VendorRecentOrderAdapter
    lateinit var ordrlist: ArrayList<com.koraspond.washershub.Models.vendorModels.getVendorOrder.Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_home)
        binding = DataBindingUtil.setContentView(this@VendorHome, R.layout.activity_vendor_home)
        val vendorRepo = VendorRepostiry.getInstance()
        viewModel = ViewModelProvider(this, VendorApisViewModelFactory(vendorRepo))
            .get(VendoApisViewModel::class.java)
        ordrlist = ArrayList()
        adapter = VendorRecentOrderAdapter(1, ordrlist)
        binding.histRcv.adapter = adapter



        var progress = ProgressDialog(this@VendorHome)
        viewModel.isLoading.observe(this) {
            if (it.equals("false")) {
                progress.dismiss()
            } else {
                progress.show()
            }
        }
        binding.createOrder.setOnClickListener {
            var intent = Intent(this@VendorHome, VendorDetailsActivity::class.java)
            intent.putExtra("id",UserInfoPreference(this).getStr("vid")!!.toInt())
            startActivity(intent)
        }

        binding.histRcv.layoutManager = LinearLayoutManager(this)
        //  binding.histRcv.adapter = HistoryAdapter(2)
        binding.menu.setOnClickListener {
            if (!binding.myDrawerLayout.isDrawerOpen(Gravity.LEFT)) binding.myDrawerLayout.openDrawer(
                Gravity.LEFT
            ) else binding.myDrawerLayout.closeDrawer(
                Gravity.RIGHT
            )
        }


        binding.nav.setNavigationItemSelectedListener(this)
        binding.nav.menu.findItem(R.id.home).isChecked = true


        setSortSpinner()


    }

    override fun onResume() {
        super.onResume()

        val headerView = binding.nav.getHeaderView(0)
        val navHeaderName = headerView.findViewById<TextView>(R.id.name)
        val navHeaderEmail = headerView.findViewById<TextView>(R.id.email)
        val navPRofile = headerView.findViewById<CircleImageView>(R.id.profile)
        Glide.with(this@VendorHome).load(StaticClass.IMAGEURL +UserInfoPreference(this@VendorHome).getStr("image")).into(navPRofile)



        // Replace with actual user data


        navHeaderName.text = UserInfoPreference(this).getStr("name")
        navHeaderEmail.text = UserInfoPreference(this).getStr("email")


        headerView.setOnClickListener {
            startActivity(Intent(this@VendorHome, ProfileActivity::class.java))

        }
        fetchRecentOrder()
    }

    private fun fetchRecentOrder() {

        val token = UserInfoPreference(this).getStr("token").toString()
        val id = UserInfoPreference(this).getStr("vid").toString()
        viewModel.getVendorOrders(token, id).observe(this@VendorHome, Observer { resource ->
            resource?.let {
                ordrlist.clear()

                if (it.data?.data != null) {


                    ordrlist.addAll(it.data.data)




                }

                else {
                    Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
            } ?: run {
               // Toast.makeText(this, "Resource is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.earning -> {
                var intent = Intent(this@VendorHome, EarningsList::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.services -> {
                var intent = Intent(this@VendorHome, VendorServicesList::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }
            R.id.history -> {
                var intent = Intent(this@VendorHome, HistoryActivity::class.java)
                startActivity(intent)
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.logout -> {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                UserInfoPreference(this).removeAllPrefData()
                finish()
                binding.myDrawerLayout.closeDrawer(Gravity.LEFT)
            }

        }
        return false
    }

    fun setSortSpinner() {
        var areas: ArrayList<String> = ArrayList<String>()
        areas.add("Ascending")
        areas.add("Descending")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = arrayAdapter


    }
}