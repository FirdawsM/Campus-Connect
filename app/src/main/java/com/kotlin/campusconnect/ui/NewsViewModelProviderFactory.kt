package com.kotlin.campusconnect.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.campusconnect.repository.firebase.FirebaseNewsRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository

class NewsViewModelProviderFactory(
    private val app: Application,
    private val newsRemoteRepository: NewsRemoteRepository,
    private val firebaseNewsRepository: FirebaseNewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(app, newsRemoteRepository, firebaseNewsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}