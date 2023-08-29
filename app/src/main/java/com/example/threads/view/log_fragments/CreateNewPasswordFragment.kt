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
import com.example.threads.databinding.FragmentCreatePasswordBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNewPasswordFragment : Fragment() {

    private lateinit var binding: FragmentCreatePasswordBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
        isSuccess()
        createNewPassword()
    }

    private fun createNewPassword() {
        binding.btnCreateNewPassword.setOnClickListener {
            val code = Holder.otpCode
            val password = binding.etNewPassword.text.toString()
            val repeat_password = binding.etNewPasswordRepeat.text.toString()

            if (password == repeat_password) {
                loadingDialogUtil.showLoadingDialog()
                lifecycleScope.launch {
                    authViewModel.createNewPassword(code, password, repeat_password)
                }
            } else {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isSuccess() {
        authViewModel.newPasswordStatus.observe(viewLifecycleOwner) { isSuccess ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "New password created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_createPasswordFragment_to_loginFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error occurred. Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigation() {
        binding.btnReturnToResetPage.setOnClickListener {
            findNavController().navigate(R.id.action_createPasswordFragment_to_forgotPasswordFragment)
        }
    }
}