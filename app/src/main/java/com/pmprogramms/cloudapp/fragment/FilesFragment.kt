package com.pmprogramms.cloudapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentFilesBinding
import com.pmprogramms.cloudapp.helpers.Codes
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel


class FilesFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var firebaseViewModel: FirebaseViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFilesBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)

        binding.uploadFileButton.setOnClickListener {
            val popup = PopupMenu(
                requireContext(),
                it!!
            )
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_upload)
            popup.show()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Codes.IMAGE_CODE_RESULT -> {
                    val dataIntent = data?.data
                    if (dataIntent != null) {
                        val filePath = data.data
                        firebaseViewModel.uploadFile(FileType.IMAGE, filePath!!)
                    }
                    return
                }
                Codes.VIDEO_CODE_RESULT-> {
                    val dataIntent = data?.data
                    if (dataIntent != null) {
                        val filePath = data.data
                        firebaseViewModel.uploadFile(FileType.VIDEO, filePath!!)
                    }
                    return
                }

                Codes.PDF_CODE_RESULT-> {
                    val dataIntent = data?.data
                    if (dataIntent != null) {
                        val filePath = data.data
                        firebaseViewModel.uploadFile(FileType.PDF, filePath!!)
                    }
                    return
                }
            }

        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.upload_image -> {
                val intentFile = Intent(Intent.ACTION_GET_CONTENT)
                intentFile.type = "image/*"
                startActivityForResult(intentFile, Codes.IMAGE_CODE_RESULT)
                true
            }
            R.id.upload_video -> {
                val intentFile = Intent(Intent.ACTION_GET_CONTENT)
                intentFile.type = "video/*"
                startActivityForResult(intentFile, Codes.VIDEO_CODE_RESULT)
                true
            }
            R.id.upload_pdf -> {
                val intentFile = Intent(Intent.ACTION_GET_CONTENT)
                intentFile.type = "application/pdf"
                startActivityForResult(intentFile, Codes.PDF_CODE_RESULT)
                true
            }
            else -> {
                Toast.makeText(context, "Unknown action.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}