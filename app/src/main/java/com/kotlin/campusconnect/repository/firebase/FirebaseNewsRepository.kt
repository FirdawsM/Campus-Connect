package com.kotlin.campusconnect.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.campusconnect.models.Article
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseNewsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveArticle(article: Article): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser
            if (user != null && article.publishedAt != null) {
                db.collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(article.publishedAt)
                    .set(article)
                    .await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFavoriteNews() = flow {
        try {
            val user = auth.currentUser
            if (user != null) {
                val snapshot = db.collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .get()
                    .await()

                val articles = snapshot.toObjects(Article::class.java)
                emit(Resource.Success(articles))
            } else {
                emit(Resource.Error("User not logged in"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error fetching favorites"))
        }
    }

    suspend fun deleteArticle(article: Article): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser
            if (user != null && article.publishedAt != null) {
                db.collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(article.publishedAt)
                    .delete()
                    .await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getFavoriteNewsByPublishedAt(publishedAt: String): Article? = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser
            if (user != null) {
                val document = db.collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(publishedAt)
                    .get()
                    .await()

                document.toObject(Article::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}