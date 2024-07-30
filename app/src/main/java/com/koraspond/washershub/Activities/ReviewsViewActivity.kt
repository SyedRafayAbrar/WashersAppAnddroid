package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.koraspond.washershub.Adapters.ReviewsAdapter
import com.koraspond.washershub.Models.getReview.Data
import com.koraspond.washershub.Models.getReview.GetReviews
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.ActivityReviewsViewBinding
import com.pegasus.pakbiz.network.Api
import retrofit2.Call
import retrofit2.Response

class ReviewsViewActivity : AppCompatActivity() {

    lateinit var reviewsAdapter: ReviewsAdapter
    lateinit var binding: ActivityReviewsViewBinding
    lateinit var reviewsList:ArrayList<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews_view)

        reviewsList = ArrayList()
        binding =
            DataBindingUtil.setContentView(this@ReviewsViewActivity, R.layout.activity_reviews_view)
        reviewsAdapter = ReviewsAdapter(reviewsList)
        binding.rateViewRcv.layoutManager = LinearLayoutManager(this@ReviewsViewActivity)
        binding.rateViewRcv.adapter = reviewsAdapter
        getReviews()

    }


    fun getReviews() {
        var progress = ProgressDialog(this@ReviewsViewActivity)
        progress.show()

        Api.client.getReviews("token "+UserInfoPreference(this@ReviewsViewActivity).getStr("token").toString(),intent.getStringExtra("id"))
            .enqueue(object :retrofit2.Callback<GetReviews>{
                override fun onResponse(call: Call<GetReviews>, response: Response<GetReviews>) {
                    progress.dismiss()
                    if(response.isSuccessful){
                        if(response.body()!=null && response.body()!!.data!=null && response.body()!!.data!!.size>0){
                            reviewsList.addAll(response.body()!!.data!!)
                            reviewsAdapter.notifyDataSetChanged()
                        }
                        else{
                            Toast.makeText(this@ReviewsViewActivity,"Connectivity error"+response.code().toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@ReviewsViewActivity,"Connectivity error"+response.code().toString(),Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<GetReviews>, t: Throwable) {
                   progress.dismiss()
                    Toast.makeText(this@ReviewsViewActivity,t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })


    }
}