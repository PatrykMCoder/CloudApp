package com.pmprogramms.cloudapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.cloudapp.model.User
import com.pmprogramms.cloudapp.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FirebaseRepository = FirebaseRepository()
    val user: MutableLiveData<User>? = repository.getUser()

    fun signInWithEmail(email: String, password: String): MutableLiveData<User> {
        return repository.signInWithEmail(email, password)
    }

    fun createUserWithEmail(email: String, password: String): MutableLiveData<User> {
        return repository.createUserWithEmail(email, password)
    }

    fun uploadFile(filePath: Uri) {
        repository.uploadFile(filePath)
    }

    fun logoutUser() {
        repository.logoutUser()
    }
}