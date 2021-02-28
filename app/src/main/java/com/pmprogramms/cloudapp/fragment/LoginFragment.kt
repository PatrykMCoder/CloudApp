package com.pmprogramms.cloudapp.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.databinding.FragmentLoginBinding
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel

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
                val progressBar = ProgressDialog.show(context, "Login...", "Please wait")
                if (it != null) {
                    progressBar.cancel()
                    findNavController().navigate(R.id.action_loginFragment_to_userFragment)
                }
            })

        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }
}