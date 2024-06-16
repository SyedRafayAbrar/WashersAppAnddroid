package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ReviewsAdapter
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityReviewsViewBinding

class ReviewsViewActivity : AppCompatActivity() {

    lateinit var reviewsAdapter: ReviewsAdapter
    lateinit var binding:ActivityReviewsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews_view)

        binding = DataBindingUtil.setContentView(this@ReviewsViewActivity,R.layout.activity_reviews_view)
        reviewsAdapter = ReviewsAdapter()
        binding.rateViewRcv.layoutManager = LinearLayoutManager(this@ReviewsViewActivity)
        binding.rateViewRcv.adapter = reviewsAdapter



    }
}