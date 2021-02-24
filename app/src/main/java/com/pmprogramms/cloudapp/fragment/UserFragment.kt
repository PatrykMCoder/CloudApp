package com.pmprogramms.cloudapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentUserBinding
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel

class UserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(layoutInflater)
        val firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)

        lifecycleScope.launchWhenStarted {
            val user = firebaseViewModel.user

            if (user != null) {
                user.observe(viewLifecycleOwner, { u ->
                    binding.test.text = u.email
                })
            } else {
                Toast.makeText(context, "Please, login to your account", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            }
        }



        return binding.root
    }
}