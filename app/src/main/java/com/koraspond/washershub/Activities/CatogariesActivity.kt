 package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Adapters.CategoriesFilterAdapter
import com.koraspond.washershub.Models.getModel.Data
import com.koraspond.washershub.Models.getModel.GetCategories
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.ClikInt
import com.pegasus.pakbiz.network.Api
import retrofit2.Call
import retrofit2.Response

 class CatogariesActivity : AppCompatActivity(), ClikInt {

     lateinit var recyclerView: RecyclerView
     lateinit var backBtn: ImageView
     lateinit var list: ArrayList<Data>
     var selectedCatId: Int? = null

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_catogaries)

         list = ArrayList()
         selectedCatId = intent.getIntExtra("selectedCatId", -1)  // Get the passed category ID

         backBtn = findViewById(R.id.back)
         backBtn.setOnClickListener {
             finish()
         }

         recyclerView = findViewById(R.id.cat_rcv)
         recyclerView.layoutManager = LinearLayoutManager(this)

         val progress = ProgressDialog(this@CatogariesActivity)
         progress.show()

         Api.client.getCategories("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe", 1).enqueue(
             object : retrofit2.Callback<GetCategories> {
                 override fun onResponse(call: Call<GetCategories>, response: Response<GetCategories>) {
                     progress.dismiss()
                     if (response.isSuccessful) {
                         response.body()?.data?.let { categories ->
                             list.addAll(categories)
                             recyclerView.adapter = CategoriesFilterAdapter(this@CatogariesActivity, list, this@CatogariesActivity, selectedCatId)
                         } ?: run {
                             Toast.makeText(this@CatogariesActivity, "No category found", Toast.LENGTH_SHORT).show()
                         }
                     } else {
                         Toast.makeText(this@CatogariesActivity, "Connectivity error: ${response.code()}", Toast.LENGTH_SHORT).show()
                     }
                 }

                 override fun onFailure(call: Call<GetCategories>, t: Throwable) {
                     progress.dismiss()
                     Toast.makeText(this@CatogariesActivity, t.message, Toast.LENGTH_SHORT).show()
                 }
             }
         )
     }

     override fun onClick(pos: Int) {
         setResult(RESULT_OK, Intent().apply {
             putExtra("catId", list[pos].id)
         })
         finish()
     }
 }

