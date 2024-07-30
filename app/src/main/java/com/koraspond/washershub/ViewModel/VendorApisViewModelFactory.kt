package com.koraspond.washershub.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Repositories.VendorRepostiry

class VendorApisViewModelFactory(private val vendorRepostiry: VendorRepostiry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VendoApisViewModel::class.java)) {
            return VendoApisViewModel(vendorRepostiry) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
