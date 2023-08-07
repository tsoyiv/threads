package com.example.threads.view.log_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
    }

    private fun navigation() {
        binding.btnReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }

        binding.btnReset.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_otpFragment)
        }
    }
}