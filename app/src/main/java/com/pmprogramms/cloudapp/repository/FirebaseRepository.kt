package com.pmprogramms.cloudapp.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pmprogramms.cloudapp.model.User
import java.io.File
import java.io.FileInputStream
import java.security.cert.CertPath

class FirebaseRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getUser(): MutableLiveData<User>? {
        val firebaseUser = firebaseAuth.currentUser
        val mutableLiveData = MutableLiveData<User>()

        return if (firebaseUser != null) {
            val uid = firebaseUser.uid
            val name = firebaseUser.displayName
            val firebaseEmail = firebaseUser.email
            val user = User(uid, name.toString(), firebaseEmail.toString())
            mutableLiveData.value = user

            mutableLiveData

        } else
            null
    }

    fun signInWithEmail(email: String, password: String): MutableLiveData<User> {
        val mutableLiveData = MutableLiveData<User>()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { auth ->
            run {
                if (auth.isSuccessful) {
                    val isNewUser = auth.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser

                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val firebaseEmail = firebaseUser.email
                        val user = User(uid, name.toString(), firebaseEmail.toString())

                        mutableLiveData.value = user
                    }
                } else {
                    //handle error here
                }
            }
        }
        return mutableLiveData
    }

    fun createUserWithEmail(email: String, password: String): MutableLiveData<User> {
        val mutableLiveData = MutableLiveData<User>()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { auth ->
            run {
                val firebaseUser = firebaseAuth.currentUser

                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val firebaseEmail = firebaseUser.email
                    val user = User(uid, name.toString(), firebaseEmail.toString())

                    mutableLiveData.value = user
                }
            }
        }

        return mutableLiveData
    }

    fun uploadFile(filePath: Uri) {
        val file = File(filePath.path)
        val storageRef = Firebase.storage.reference.child("files/${firebaseAuth.currentUser?.uid}/image/${file.name}")
        val uploadTask = storageRef.putFile(filePath)

        uploadTask.addOnSuccessListener { task ->
            run {
                if (task.task.isComplete)
                    Log.d("uploadFile", "uploadFile: FINISHED UPLOAD")
            }
        }
    }

    fun logoutUser() {
        firebaseAuth.signOut()
    }
}