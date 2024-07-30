package com.koraspond.washershub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope

import com.koraspond.washershub.Models.SignupModel.SignupModel
import com.koraspond.washershub.Models.SignupModel.SignupRequestModel
import com.koraspond.washershub.Repositories.LoginRepository
import com.koraspond.washershub.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(var loginRepository: LoginRepository) : ViewModel() {
    var isloading: MutableLiveData<String> = loginRepository.isLoading


    fun getSignup(signupRequestModel: SignupRequestModel): LiveData<Resource<SignupModel>?>? {
        var liveData: LiveData<Resource<SignupModel>?>? = null
        viewModelScope.launch(Dispatchers.IO) {
            liveData = loginRepository.attemptSignup(signupRequestModel)
        }
        return liveData
    }


}