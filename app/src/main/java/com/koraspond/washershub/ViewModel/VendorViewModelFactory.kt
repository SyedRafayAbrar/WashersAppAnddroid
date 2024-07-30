package com.koraspond.washershub.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koraspond.washershub.Repositories.CustomerRepos

class VendorViewModelFactory(private val customerRepos: CustomerRepos) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VendorViewModel::class.java)) {
            return VendorViewModel(customerRepos) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
