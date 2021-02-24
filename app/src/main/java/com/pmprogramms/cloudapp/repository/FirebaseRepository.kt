package com.pmprogramms.cloudapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pmprogramms.cloudapp.model.User

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
}