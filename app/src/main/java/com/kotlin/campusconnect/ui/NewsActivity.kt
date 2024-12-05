package com.kotlin.campusconnect.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.databinding.ActivityNewsBinding
import com.kotlin.campusconnect.db.ArticleDatabase
import com.kotlin.campusconnect.repository.local.NewsLocalRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRemoteRepository = NewsRemoteRepository()
        val newsLocalRepository = NewsLocalRepository(ArticleDatabase(this))
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(application, newsRemoteRepository, newsLocalRepository)

        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navHostController)

    }
}