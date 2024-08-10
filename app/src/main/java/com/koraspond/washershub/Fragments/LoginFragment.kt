package com.koraspond.washershub.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.koraspond.washershub.Activities.HomeActivity
import com.koraspond.washershub.Activities.VendorHome
import com.koraspond.washershub.Models.loginModel.LoginModel
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.FragmentLoginBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {
lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.login.setOnClickListener {
            val progress = ProgressDialog(context)

            if(!binding.nameEt.text.toString().trim().isEmpty()){
                if(!binding.passwordEt.text.toString().trim().isEmpty()){
                    progress.show()
                    var signupRequestModel=  LoginRequestModel(binding.nameEt.text.toString().trim(),binding.passwordEt.text.toString().trim())
                    val jsonObject = JSONObject().apply {
                        put("username", signupRequestModel.name)
                        put("password", signupRequestModel.password)

                    }
                    val body: RequestBody
                    body = jsonObject.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    Api.client.login("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe",body).enqueue(object :retrofit2.Callback<LoginModel>{
                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            progress.dismiss()
                            if(response.isSuccessful ){
                                if(response.body()?.status==200 && response.body()!=null){
                                    var userInfoPreference = UserInfoPreference(requireContext())
                                    userInfoPreference.setStr("isLogin","true")
                                    userInfoPreference.setStr("id",response.body()!!.data.id.toString())
                                    userInfoPreference.setStr("name",response.body()!!.data.user_name)
                                    userInfoPreference.setStr("email",response.body()!!.data.email)
                                    userInfoPreference.setStr("token",response.body()!!.data.token)
                                    userInfoPreference.setStr("contact",response.body()!!.data.user_info.contact_number)
                                    userInfoPreference.setStr("image",response.body()!!.data.user_info.image?:"")



                                    if(response.body()!!.data.user_role.id==2){
                                        userInfoPreference.setStr("role","c")
                                    var  intent = Intent(requireContext(),HomeActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else{
                                        userInfoPreference.setStr("role","v")
                                        userInfoPreference.setStr("vname",response.body()!!.data.vendor.shop_name)
                                        userInfoPreference.setStr("vid",response.body()!!.data.vendor.id.toString())
                                        var  intent = Intent(requireContext(),VendorHome::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()

                                    }                                }
                                else{
//                                    val errorBody = response.errorBody()?.string()
//                                    val errorMessage = parseErrorMessage(errorBody)

                                    Toast.makeText(requireContext(), response.body()?.error?:"error", Toast.LENGTH_SHORT).show()

                                }
                            }
                            else{
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = parseErrorMessage(errorBody)

                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            progress.dismiss()
                            Toast.makeText(context, "failure:"+t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                else{
                    binding.passwordEt.error = "Please enter password"
                }
            }
            else{
                binding.nameEt.error = "Please enter user name"
            }

        }
    }
    private fun parseErrorMessage(errorBodyString: String?): String {
        if (errorBodyString.isNullOrBlank()) {
            return "Unknown error"
        }

        try {
            val jsonObject = JSONObject(errorBodyString)
            if (jsonObject.has("error")) {
                return jsonObject.getString("error")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return "Unknown error"
    }

}