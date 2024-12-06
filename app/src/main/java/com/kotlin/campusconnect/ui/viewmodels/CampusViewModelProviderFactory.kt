package com.kotlin.campusconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.campusconnect.repository.remote.CampusRepository

class CampusViewModelProviderFactory(
    private val campusRepository: CampusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CampusViewModel(campusRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}