package com.kotlin.campusconnect.repository.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.campusconnect.models.Poster
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CampusRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPosters(): Flow<Resource<List<Poster>>> = flow {
        try {
            emit(Resource.Loading)
            val querySnapshot = firestore.collection("posters").get().await()
            val posters = querySnapshot.documents.mapNotNull { it.toObject(Poster::class.java) }
            emit(Resource.Success(posters))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun createPoster(title: String, description: String, imageUri: Uri?) {
        val poster = Poster(
            title = title,
            description = description,
            imageUrl = imageUri?.toString(),
            userId = FirebaseAuth.getInstance().currentUser?.uid,
            userName = FirebaseAuth.getInstance().currentUser?.displayName,
            createdAt = System.currentTimeMillis()
        )
        firestore.collection("posters").add(poster).await()
    }

    suspend fun deletePoster(poster: Poster) {
        firestore.collection("posters")
            .whereEqualTo("userId", poster.userId)
            .whereEqualTo("createdAt", poster.createdAt)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.reference
            ?.delete()
            ?.await()
    }
}