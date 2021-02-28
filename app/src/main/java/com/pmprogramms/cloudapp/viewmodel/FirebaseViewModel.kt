package com.pmprogramms.cloudapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.model.File
import com.pmprogramms.cloudapp.model.User
import com.pmprogramms.cloudapp.repository.FirebaseRepository

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FirebaseRepository = FirebaseRepository()
    val user: MutableLiveData<User>? = repository.getUser()


    fun signInWithEmail(email: String, password: String): MutableLiveData<User> {
        return repository.signInWithEmail(email, password)
    }

    fun createUserWithEmail(email: String, password: String): MutableLiveData<User> {
        return repository.createUserWithEmail(email, password)
    }

    fun uploadFile(fileType: FileType, filePath: Uri) {
        repository.uploadFile(fileType, filePath)
    }

    fun getAllFilesList(): MutableLiveData<ArrayList<File>> {
        return repository.getAllFilesList()
    }

    fun logoutUser() {
        repository.logoutUser()
    }
}