package com.example.threads.view.log_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentOtpBinding

class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
    }

    private fun navigation() {
        binding.btnReturnToResetEmailPage.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_forgotPasswordFragment)
        }

        binding.btnNewPassPage.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_createPasswordFragment)
        }
    }
}