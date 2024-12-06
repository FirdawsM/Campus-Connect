package com.kotlin.campusconnect.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.campusconnect.models.Poster
import com.kotlin.campusconnect.repository.remote.CampusRepository
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CampusViewModel(
    private val campusRepository: CampusRepository
) : ViewModel() {
    suspend fun getPosters(): Flow<Resource<List<Poster>>> {
        return campusRepository.getPosters()
    }

    fun createPoster(title: String, description: String, imageUri: Uri?) {
        viewModelScope.launch {
            campusRepository.createPoster(title, description, imageUri)
        }
    }

    fun deletePoster(poster: Poster) {
        viewModelScope.launch {
            campusRepository.deletePoster(poster)
        }
    }

    fun addPoster(poster: Poster) {
        viewModelScope.launch {
            campusRepository.createPoster(poster.title, poster.description, Uri.parse(poster.imageUrl))
        }
    }
}