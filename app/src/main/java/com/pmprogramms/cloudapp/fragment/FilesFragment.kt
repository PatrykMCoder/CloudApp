package com.pmprogramms.cloudapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pmprogramms.cloudapp.databinding.FragmentFilesBinding


class FilesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFilesBinding.inflate(layoutInflater)
        return binding.root
    }
}