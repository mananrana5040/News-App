package com.example.shared.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.repository.BookmarkRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

class AuthViewModel(private val bookmarkRepository: BookmarkRepository) : ViewModel() {

    private val auth = Firebase.auth

    var authError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun signUp(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                auth.createUserWithEmailAndPassword(email, pass)
                onSuccess()
            } catch (e: Exception) {
                authError = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                auth.signInWithEmailAndPassword(email, pass)
                onSuccess()
            } catch (e: Exception) {
                authError = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                bookmarkRepository.clearLocalData()
                auth.signOut()
            } catch (e: Exception) {
            }
        }
    }
}