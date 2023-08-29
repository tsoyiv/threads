package com.example.threads.view.log_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentOtpBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
        isSuccess()
        otpVerify()
    }

    private fun otpVerify() {
        val pinCode = binding.pinView
        binding.btnNewPassPage.setOnClickListener {
            val code = pinCode.text.toString()
            Holder.otpCode = code

            loadingDialogUtil.showLoadingDialog()

            lifecycleScope.launch {
                authViewModel.otpCodeRequest(code)
            }
        }

        binding.txtResendCode.setOnClickListener {
            val email = Holder.email

            Toast.makeText(requireContext(), "OTP code has been sent once again", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                authViewModel.sendForgotPasswordEmail(email)
            }
        }
    }

    private fun isSuccess() {
        authViewModel.otpStatus.observe(viewLifecycleOwner) { isSuccess ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Correct Code, please change the password",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_otpFragment_to_createPasswordFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error Occurred. Please, try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigation() {
        binding.btnReturnToResetEmailPage.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_forgotPasswordFragment)
        }
    }
}