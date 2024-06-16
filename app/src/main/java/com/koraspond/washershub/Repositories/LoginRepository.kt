package com.koraspond.washershub.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koraspond.washershub.Models.SignupModel.SignupModel
import com.koraspond.washershub.Models.SignupModel.SignupRequestModel
import com.koraspond.washershub.Resource
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import kotlin.math.sin


class LoginRepository {

    var isLoading: MutableLiveData<String> = MutableLiveData()
    var userData: MutableLiveData<Resource<SignupModel>?>? = null

    companion object {
        var authRepository: LoginRepository? = null

    }

    fun getAuthRepository(): LoginRepository {
        if (authRepository == null) {
            authRepository = LoginRepository()
        }
        return authRepository!!
    }

    suspend fun attemptSignup(signupRequestModel: SignupRequestModel): LiveData<Resource<SignupModel>?>? {
        if (signupRequestModel.email.toString().isEmpty()) {
            return userData
        }
        userData = MutableLiveData()
        isLoading.value = "true"

        val jsonObject = JSONObject().apply {
            put("username", signupRequestModel.username)
            put("email", signupRequestModel.email)
            put("password", signupRequestModel.password)
            put("contact_number", signupRequestModel.contactNumber)
            put("role", signupRequestModel.role)
        }

        val body: RequestBody
        body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonObject.toString()
        )

//        Api.client.userSignup(body).enqueue(object : retrofit2.Callback<SignupModel> {
//            override fun onResponse(call: Call<SignupModel>, response: Response<SignupModel>) {
//
//                if (response.isSuccessful) {
//                    if (response.body()!!.status == 200) {
//                        val signupModel: SignupModel = response.body()!!
//
//                        userData?.value = Resource.success(signupModel)
//
//
//                    } else {
//
//                        userData?.value = Resource.error(null, "not able")
//                    }
//                } else {
//                    userData?.value = Resource.error(null, "error_" + response.code().toString())
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<SignupModel>, t: Throwable) {
//                isLoading.value = "false"
//                userData?.value = Resource.error(null, t.message!!.toString())
//            }
//
//        })

        return userData
    }

}