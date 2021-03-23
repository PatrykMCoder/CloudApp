package com.pmprogramms.cloudapp.repository

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.storage
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.helpers.UploadState
import com.pmprogramms.cloudapp.model.User
import java.io.File
import com.pmprogramms.cloudapp.model.File as fileModel

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

    fun uploadFile(fileType: FileType, filePath: Uri) : LiveData<UploadState> {
        val file = File(filePath.path)
        val uploadStateLiveData = MutableLiveData<UploadState>()
        uploadStateLiveData.value = UploadState.INGOING

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
                if (task.task.isComplete) {
                    uploadStateLiveData.value = UploadState.SUCCESS
                    Log.d("uploadFile", "uploadFile: FINISHED UPLOAD")
                }else
                    uploadStateLiveData.value = UploadState.FAIL
            }
        }

        return uploadStateLiveData
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
                val file = fileModel(item.name, item.path, FileType.IMAGE)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        storageVideoRef.listAll().addOnSuccessListener { (items, prefixes) ->
            prefixes.forEach { prefix ->
            }

            items.forEach { item ->
                val file = fileModel(item.name, item.path, FileType.VIDEO)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        storagePDFRef.listAll().addOnSuccessListener { (items, prefixes) ->
            prefixes.forEach { prefix ->
            }

            items.forEach { item ->
                val file = fileModel(item.name, item.path, FileType.PDF)
                listOfFiles.add(file)
                mutableLiveData.value = listOfFiles
            }
        }

        return mutableLiveData
    }

    fun downloadFile(path: String, fileType: FileType) {
        val storageRef = Firebase.storage.reference.child(path)
        val localFile = when (fileType) {
            FileType.IMAGE -> {
                File.createTempFile("image", ".jpg")
            }
            FileType.VIDEO -> {
                File.createTempFile("video", ".mp4")
            }
            FileType.PDF -> {
                File.createTempFile("pdf", ".pdf")
            }
        }

        storageRef.getFile(localFile)
            .addOnSuccessListener {
                Log.d("downloadFile", "downloadFile: downloaded file, ${localFile.path}")
                // todo move file from app cache to download directory
            }
            .addOnFailureListener {
                Log.d("downloadFile", "downloadFile: download file fail: ${it.message}")
            }
    }

    fun logoutUser() {
        firebaseAuth.signOut()
    }
}