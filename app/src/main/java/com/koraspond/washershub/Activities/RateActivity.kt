package com.koraspond.washershub.Activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.koraspond.washershub.Models.AddReview.AddReview
import com.koraspond.washershub.Models.addVendorModel.AddRatingModel
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.ActivityHistoryDetailBinding
import com.koraspond.washershub.databinding.ActivityRateBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RateActivity : AppCompatActivity() {
    lateinit var binding: ActivityRateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)
        binding = DataBindingUtil.setContentView(this@RateActivity, R.layout.activity_rate)
        binding.backBtn.setOnClickListener {
            finish()

        }

        binding.rateBtn.setOnClickListener {
            val jsonObject = JSONObject().apply {
                put("vendor", intent.getStringExtra("vendor"))
                put("user", intent.getStringExtra("user"))
                put("order", intent.getStringExtra("order"))
                put("rating", binding.ratingDialog.rating)
                put("review", binding.reviewEt.text.toString())

            }
            val progressDialog = ProgressDialog(this)
            progressDialog.show()
            val body: RequestBody
            body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val token = UserInfoPreference(this).getStr("token").toString()

            Api.client.addRating("token " + token, body)
                .enqueue(object : retrofit2.Callback<AddReview> {
                    override fun onResponse(
                        call: Call<AddReview>,
                        response: Response<AddReview>
                    ) {
                        progressDialog.dismiss()
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body()!!.status == 201) {
                                Toast.makeText(
                                    this@RateActivity,
                                    "review added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RateActivity,
                                    "connectivity error " + response.code(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(
                                this@RateActivity,
                                "connectivity error " + response.code(),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<AddReview>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(this@RateActivity, t.message, Toast.LENGTH_SHORT).show()

                    }

                })

        }
    }


}
