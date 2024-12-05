package com.kotlin.campusconnect.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.campusconnect.repository.local.NewsLocalRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository

class NewsViewModelProviderFactory(
    val app: Application,
    private val newsRemoteRepository: NewsRemoteRepository,
    private val newsLocalRepository: NewsLocalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRemoteRepository, newsLocalRepository) as T
    }
}