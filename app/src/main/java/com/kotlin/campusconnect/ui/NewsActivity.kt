package com.kotlin.campusconnect.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.databinding.ActivityNewsBinding
import com.kotlin.campusconnect.repository.firebase.FirebaseNewsRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModel
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModelProviderFactory


class NewsActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRemoteRepository = NewsRemoteRepository()
        val firebaseNewsRepository = FirebaseNewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(
            application,
            newsRemoteRepository,
            firebaseNewsRepository
        )

        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navHostController)
    }
}