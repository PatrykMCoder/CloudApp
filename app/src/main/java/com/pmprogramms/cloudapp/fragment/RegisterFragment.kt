package com.pmprogramms.cloudapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
            val email = binding.emailEditText.text.trim().toString()
            val password = binding.passwordEdit.text.toString()
            val user = firebaseViewModel.createUserWithEmail(email, password)

//          TODO make better solution for this
            user.observe(viewLifecycleOwner, {
                if (it != null)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            })


        }

        return binding.root
    }
}