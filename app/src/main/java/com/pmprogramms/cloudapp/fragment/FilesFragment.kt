package com.pmprogramms.cloudapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pmprogramms.cloudapp.helpers.Codes
import com.pmprogramms.cloudapp.databinding.FragmentFilesBinding
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel


class FilesFragment : Fragment() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFilesBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding.uploadFileButton.setOnClickListener {
            val intentFile = Intent(Intent.ACTION_GET_CONTENT)
            intentFile.type = "image/*"
            startActivityForResult(intentFile, Codes.FILE_CODE_RESULT)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Codes.FILE_CODE_RESULT -> {
                    val dataIntent = data?.data
                    if (dataIntent != null) {
                        val filePath = data.data
                        firebaseViewModel.uploadFile(filePath!!)

                    }
                }
            }

        }
    }
}