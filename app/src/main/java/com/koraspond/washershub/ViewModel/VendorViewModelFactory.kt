package com.koraspond.washershub.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koraspond.washershub.Repositories.VendorRepo

class VendorViewModelFactory(private val vendorRepo: VendorRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VendorViewModel::class.java)) {
            return VendorViewModel(vendorRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
