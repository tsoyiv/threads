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
import com.example.threads.databinding.FragmentForgotPasswordBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
        sendEmail()
        isSuccess()
    }

    private fun sendEmail() {
        binding.btnReset.setOnClickListener {
            val email = binding.etEmailResPassword.text.toString()

            Holder.email = email
            loadingDialogUtil.showLoadingDialog()

            lifecycleScope.launch {
                authViewModel.sendForgotPasswordEmail(email)
            }
        }
    }

    private fun isSuccess() {
        authViewModel.forgotPasswordStatus.observe(viewLifecycleOwner) { isSuccess ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Check Email, code has been sent",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_otpFragment)
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
        binding.btnReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }
}