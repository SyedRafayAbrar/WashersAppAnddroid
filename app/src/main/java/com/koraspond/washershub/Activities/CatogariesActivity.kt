 package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Adapters.CategoriesFilterAdapter
import com.koraspond.washershub.R

class CatogariesActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var backBtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catogaries)

        backBtn =findViewById(R.id.back)
        backBtn.setOnClickListener {
            finish()
        }
        recyclerView =findViewById(R.id.cat_rcv)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoriesFilterAdapter(this)
    }
}