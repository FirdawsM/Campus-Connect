package com.kotlin.campusconnect.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.databinding.ActivityNewsBinding
import com.kotlin.campusconnect.repository.firebase.FirebaseNewsRepository
import com.kotlin.campusconnect.repository.remote.CampusRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository
import com.kotlin.campusconnect.ui.viewmodels.CampusViewModel
import com.kotlin.campusconnect.ui.viewmodels.CampusViewModelProvider
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModel
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel
    lateinit var campusViewModel: CampusViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create the NewsRemoteRepository and FirebaseNewsRepository instances
        val newsRemoteRepository = NewsRemoteRepository()
        val firebaseNewsRepository = FirebaseNewsRepository()

        // Create the NewsViewModelProviderFactory and instantiate the NewsViewModel
        val viewModelProviderFactory = NewsViewModelProviderFactory(
            application,
            newsRemoteRepository,
            firebaseNewsRepository
        )
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        // Create the CampusRepository and CampusViewModelProvider, then instantiate the CampusViewModel
        val campusRepository = CampusRepository(/* dependencies */)
        val campusViewModelProvider = CampusViewModelProvider(campusRepository)
        campusViewModel = ViewModelProvider(this, campusViewModelProvider)[CampusViewModel::class.java]

        // Set up the navigation with the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navHostController)
    }
}