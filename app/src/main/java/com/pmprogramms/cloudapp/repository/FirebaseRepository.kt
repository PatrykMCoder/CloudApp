package com.pmprogramms.cloudapp.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.model.User
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.pmprogramms.cloudapp.model.File as fileModel
import java.io.File

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

    fun uploadFile(fileType: FileType, filePath: Uri) {
        val file = File(filePath.path)

        val catalog = when (fileType) {
            FileType.IMAGE -> {
                "image"
            }
            FileType.VIDEO -> {
                "video"
            }
            FileType.PDF -> {
                "pdf"
            }
        }

        val storageRef =
            Firebase.storage.reference.child("files/${firebaseAuth.currentUser?.uid}/$catalog/${file.name}")
        val uploadTask = storageRef.putFile(filePath)

        uploadTask.addOnSuccessListener { task ->
            run {
                if (task.task.isComplete)
                    Log.d("uploadFile", "uploadFile: FINISHED UPLOAD")
            }
        }
    }


    fun getAllFilesList(): MutableLiveData<ArrayList<fileModel>> {
        val mutableLiveData = MutableLiveData<ArrayList<fileModel>>()
        val listOfFiles: ArrayList<fileModel> = ArrayList()
        val storageImageRef =
            Firebase.storage.reference.child("files/${firebaseAuth.currentUser?.uid}/image")
        val storageVideoRef =
            Firebase.storage.reference.child("files/${firebaseAuth.currentUser?.uid}/video")
        val storagePDFRef =
            Firebase.storage.reference.child("files/${firebaseAuth.currentUser?.uid}/pdf")

        storageImageRef.listAll().addOnSuccessListener { (items, prefixes) ->
            prefixes.forEach { prefix ->
            }

            items.forEach { item ->
                val file = fileModel(item.name, item.downloadUrl, FileType.IMAGE)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        storageVideoRef.listAll().addOnSuccessListener { (items, prefixes) ->
            prefixes.forEach { prefix ->
            }

            items.forEach { item ->
                val file = fileModel(item.name, item.downloadUrl, FileType.VIDEO)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        storagePDFRef.listAll().addOnSuccessListener { (items, prefixes) ->
            prefixes.forEach { prefix ->
            }

            items.forEach { item ->
                val file = fileModel(item.name, item.downloadUrl, FileType.PDF)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        return mutableLiveData
    }

    fun logoutUser() {
        firebaseAuth.signOut()
    }
}