package com.example.threads.view.log_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentConfirmEmailBinding
import com.example.threads.databinding.FragmentOtpBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmEmailFragment : Fragment() {

    private lateinit var binding: FragmentConfirmEmailBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var otpFields: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        (requireActivity() as MainActivity).hide()

        navigation()
        isSuccess()
        //otpVerify()
        etCheck()
    }

    private fun etCheck() {
        otpFields = binding.pinView
        btnLogin = binding.btnNewPassPage

        otpFields.addTextChangedListener(textWatcher)

        btnLogin.isEnabled = true

        btnLogin.setOnClickListener {
            val otpText = otpFields.text.toString()

            if (otpText.isEmpty()) {
                Toast.makeText(requireContext(), "Field must be filled", Toast.LENGTH_SHORT)
                    .show()
            } else {
                otpVerify()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btnLogin.isEnabled = true

        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun otpVerify() {
        val pinCode = binding.pinView
        val code = pinCode.text.toString()
        Holder.otpCode = code

        loadingDialogUtil.showLoadingDialog()

        lifecycleScope.launch {
            authViewModel.otpCodeRequest(code)
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
                    "User created",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_confirmEmailFragment_to_loginFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Incorrect code typed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigation() {
        binding.btnReturnToRegPage.setOnClickListener {
            findNavController().navigate(R.id.action_confirmEmailFragment_to_regProfileFragment)
        }
    }
}