package com.pmprogramms.cloudapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentFileDetailsBinding
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel

class FileDetailsFragment : Fragment() {
    private val args by navArgs<FileDetailsFragmentArgs>()
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        val binding = FragmentFileDetailsBinding.inflate(layoutInflater)
        val file = args.file

        when (file.type) {
            FileType.IMAGE -> {
                binding.typeFile.setImageResource(R.drawable.ic_baseline_image_24)
            }
            FileType.VIDEO -> {
                binding.typeFile.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
            FileType.PDF -> {
                binding.typeFile.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
            }
        }
        binding.titleFile.text = file.title

        binding.downloadFile.setOnClickListener {
            firebaseViewModel.downloadFile(file.firebasePath, file.type)
        }

        return binding.root
    }
}