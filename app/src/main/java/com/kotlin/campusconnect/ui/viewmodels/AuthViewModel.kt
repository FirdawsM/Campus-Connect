// app/src/main/java/com/kotlin/campusconnect/ui/viewmodels/AuthViewModel.kt
package com.kotlin.campusconnect.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<Resource<FirebaseUser>>()
    val authState: LiveData<Resource<FirebaseUser>> = _authState

    fun signUp(email: String, password: String) = viewModelScope.launch {
        _authState.value = Resource.Loading()
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            _authState.value = Resource.Success(result.user!!)
        } catch (e: Exception) {
            _authState.value = Resource.Error(e.message ?: "Sign up failed")
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = Resource.Loading()
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            _authState.value = Resource.Success(result.user!!)
        } catch (e: Exception) {
            _authState.value = Resource.Error(e.message ?: "Sign in failed")
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = Resource.Error("Signed out")
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}

