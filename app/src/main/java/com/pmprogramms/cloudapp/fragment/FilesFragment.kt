package com.pmprogramms.cloudapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentFilesBinding
import com.pmprogramms.cloudapp.helpers.Codes
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.util.FileRecyclerViewAdapter
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel


class FilesFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var recyclerViewAdapter: FileRecyclerViewAdapter
    private lateinit var binding: FragmentFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        recyclerViewAdapter = FileRecyclerViewAdapter()

        getAllFiles()

        binding.recyclerview.adapter = recyclerViewAdapter
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.uploadFileButton.setOnClickListener {
            val popup = PopupMenu(
                requireContext(),
                it!!
            )
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_upload)
            popup.show()
        }

        binding.swipe.setOnRefreshListener {
            getAllFiles()
        }

        return binding.root
    }

    private fun getAllFiles() {
        firebaseViewModel.getAllFilesList().observe(viewLifecycleOwner, {
            recyclerViewAdapter.setData(it)
        })

        binding.swipe.isRefreshing = false
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
                Codes.VIDEO_CODE_RESULT -> {
                    val dataIntent = data?.data
                    if (dataIntent != null) {
                        val filePath = data.data
                        firebaseViewModel.uploadFile(FileType.VIDEO, filePath!!)
                    }
                    return
                }

                Codes.PDF_CODE_RESULT -> {
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