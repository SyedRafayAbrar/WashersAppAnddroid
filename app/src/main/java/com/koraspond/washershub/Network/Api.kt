package com.pegasus.pakbiz.network


import com.koraspond.washershub.Network.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private var retrofit: Retrofit? = null
    val client: ApiInterface
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    /*.baseUrl("http://10.0.115.69:3000/")*/
                    .baseUrl("http://128.199.164.168:8000/api/")

                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
}