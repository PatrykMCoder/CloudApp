package com.pmprogramms.cloudapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentLoginBinding
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(layoutInflater)
        val firebaseViewModel = ViewModelProvider(this).get(
            FirebaseViewModel::class.java
        )
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.trim().toString()
            val password = binding.passwordEdit.text.toString()
            val user = firebaseViewModel.signInWithEmail(email, password)

//          TODO make better solution for this
            user.observe(viewLifecycleOwner, {
                if (it != null)
                    findNavController().navigate(R.id.action_loginFragment_to_userFragment)
            })

        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }
}