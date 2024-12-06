package com.kotlin.campusconnect.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.campusconnect.NewsApplication
import com.kotlin.campusconnect.models.Article
import com.kotlin.campusconnect.models.NewsResponse
import com.kotlin.campusconnect.repository.firebase.FirebaseNewsRepository
import com.kotlin.campusconnect.repository.remote.NewsRemoteRepository
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRemoteRepository: NewsRemoteRepository,
    private val firebaseNewsRepository: FirebaseNewsRepository
) : AndroidViewModel(app) {
    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    private var headlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null

    private val _isArticleSaved = MutableLiveData<Boolean>()
    val isArticleSaved: LiveData<Boolean> get() = _isArticleSaved

    init {
        getHeadlinesNews("us")
        updateArticleSaveStatus(false)
    }

    fun getHeadlinesNews(countryCode: String) = viewModelScope.launch {
        headlinesNewsRemote(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsRemote(searchQuery)
    }

    fun addNewsToFavorites(article: Article) = viewModelScope.launch {
        val result = firebaseNewsRepository.saveArticle(article)
        updateArticleSaveStatus(true)
    }

    fun getFavoriteNews() = firebaseNewsRepository.getFavoriteNews()

    fun getFavoriteNewsByPublishedAt(publishedAt: String) {
        viewModelScope.launch {
            val article = firebaseNewsRepository.getFavoriteNewsByPublishedAt(publishedAt)
            updateArticleSaveStatus(article != null)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        try {
            val result = firebaseNewsRepository.deleteArticle(article)
            updateArticleSaveStatus(!result)
        } catch (e: Exception) {
            Log.d("NewsViewModel", e.toString())
        }
    }

    private fun updateArticleSaveStatus(newValue: Boolean) {
        _isArticleSaved.value = newValue
    }

    private suspend fun headlinesNewsRemote(countryCode: String) {
        headlines.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRemoteRepository.getHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resource.Error("Unable to connect"))
                else -> headlines.postValue(Resource.Error("No signal"))
            }
        }
    }

    private suspend fun searchNewsRemote(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRemoteRepository.search(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect"))
                else -> searchNews.postValue(Resource.Error("No signal"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteArticleByPublishedAt(publishedAt: String) = viewModelScope.launch {
        try {
            firebaseNewsRepository.getFavoriteNewsByPublishedAt(publishedAt)?.let { article ->
                val result = firebaseNewsRepository.deleteArticle(article)
                updateArticleSaveStatus(!result)
            }
        } catch (e: Exception) {
            Log.d("NewsViewModel", e.toString())
        }
    }
}















