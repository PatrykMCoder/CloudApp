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
import com.pmprogramms.cloudapp.databinding.FragmentRegisterBinding
import com.pmprogramms.cloudapp.viewmodel.FirebaseViewModel

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegisterBinding.inflate(layoutInflater)
        val firebaseViewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding.registerButton.setOnClickListener {
            val progressBar = ProgressDialog.show(context, "Create account...", "Please wait")
            val email = binding.emailEditText.text.trim().toString()
            val password = binding.passwordEditText.text.toString()
            val user = firebaseViewModel.createUserWithEmail(email, password)

            user.observe(viewLifecycleOwner, {
                if (it != null) {
                    progressBar.cancel()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                progressBar.cancel()
            })
        }

        return binding.root
    }
}