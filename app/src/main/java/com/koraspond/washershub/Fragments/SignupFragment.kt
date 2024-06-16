package com.koraspond.washershub.Fragments

import android.app.ProgressDialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.koraspond.washershub.Activities.HomeActivity
import com.koraspond.washershub.Activities.VendorHome
import com.koraspond.washershub.Models.SignupModel.SignupModel
import com.koraspond.washershub.Models.SignupModel.SignupRequestModel
import com.koraspond.washershub.R
import com.koraspond.washershub.Repositories.LoginRepository
import com.koraspond.washershub.Resource
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.ViewModel.LoginViewModel
import com.koraspond.washershub.databinding.FragmentSignupBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class SignupFragment : Fragment() {
lateinit var signupVendor:MaterialButton
lateinit var binding:FragmentSignupBinding

//    lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupBinding.inflate(inflater,container,false)

        // Inflate t he layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupVendor = view.findViewById(R.id.vendor_signup)



      //  var loginRepository = LoginRepository().getAuthRepository()
//        viewModel =ViewModelProvider(this@SignupFragment,factory).get(LoginViewModel(loginRepository)::class.java)
//
//
//        val progress = ProgressDialog(context)
//
//        viewModel.isloading.observe( viewLifecycleOwner) {
//            if (it.equals("false")) {
//                progress.dismiss()
//            } else {
//                progress.show()
//            }
//        }
        val progress = ProgressDialog(context)
        binding.signup.setOnClickListener {
            Toast.makeText(context, "1.", Toast.LENGTH_SHORT).show()

            if(!binding.nameEt.text.toString().isEmpty()){
                Toast.makeText(context, "2.", Toast.LENGTH_SHORT).show()

                if(!binding.emailEt.text.toString().isEmpty()){
                        if(!binding.contactNo.text.toString().isEmpty()){
                            if(!binding.nPassword.text.toString().isEmpty()){
                                if(!binding.cPassword.text.toString().isEmpty()){
                                    if(binding.cPassword.text.toString().trim().toString().equals(binding.nPassword.text.toString().trim())){
                                     var signupRequestModel=  SignupRequestModel(binding.nameEt.text.toString().trim(),binding.emailEt.text.toString().trim(),binding.nPassword.text.toString().trim(),binding.contactNo.text.toString().trim(),
                                            "customer")
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
                                        progress.show()

                                        Api.client.userSignup("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe",body).enqueue(object : retrofit2.Callback<SignupModel> {
                                            override fun onResponse(call: Call<SignupModel>, response: Response<SignupModel>) {
                                    progress.dismiss()
                                                if (response.isSuccessful) {
                                                    if (response.body()!!.status == 200) {
                                                        var userInfoPreference = UserInfoPreference(requireContext())
                                                        userInfoPreference.setStr("isLogin","true")
                                                        userInfoPreference.setStr("id",response.body()!!.data.id.toString())
                                                        userInfoPreference.setStr("name",response.body()!!.data.user_name)
                                                        userInfoPreference.setStr("email",response.body()!!.data.email)
                                                        userInfoPreference.setStr("token",response.body()!!.data.token)
                                                        var  intent = Intent(requireContext(),
                                                            HomeActivity::class.java)
                                                        startActivity(intent)
                                                        requireActivity().finish()

                                                    } else {

                                                        Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                } else {
                                                    Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT)
                                                        .show()

                                                }

                                            }

                                            override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                                              progress.dismiss()
                                                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT)
                                                    .show()
                                            }

                                        })
                                    }
                                    else{
                                        Toast.makeText(context, "Passwords are not same.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    binding.cPassword.setError("please enter password again")
                                }
                            }
                            else{
                                binding.nPassword.setError("Please enter password")

                            }
                        }
                        else{
                            binding.contactNo.setError("Please enter contact number")
                        }
                    }
                    else{
                        binding.emailEt.setError("Please enter email")
                    }
                }
            else{
                    Toast.makeText(context, "please enter user name", Toast.LENGTH_SHORT).show()
                    binding.nameEt.setError("Please enter user name")
            }
        }
        signupVendor.setOnClickListener {
            var intent = Intent(requireContext(),VendorHome::class.java)
            requireContext().startActivity(intent)
        }
    }

}