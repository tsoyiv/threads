package com.example.threads.view.log_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
    private lateinit var etPassword: EditText
    private lateinit var etPasswordRepeat: EditText
    private lateinit var btnCreateUser: Button

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
        etCheck()
    }

    private fun etCheck() {
        etPassword = binding.etNewPassword
        etPasswordRepeat = binding.etNewPassword
        btnCreateUser = binding.btnCreateNewPassword

        etPassword.addTextChangedListener(textWatcher)
        etPasswordRepeat.addTextChangedListener(textWatcher)

        btnCreateUser.isEnabled = true

        btnCreateUser.setOnClickListener {
            val passwordText = etPassword.text.toString()
            val passwordRepeatText = etPasswordRepeat.text.toString()

            if (passwordText.isEmpty() || passwordRepeatText.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT)
                    .show()
            } else if (passwordText.length < 8 || !passwordText.any { it.isDigit() } || !passwordText.any { it.isLetter() } || !passwordText.any { !it.isLetterOrDigit() }) {
                Toast.makeText(
                    requireContext(),
                    "Password must be at least 8 characters long, contain a number, a letter, and a special symbol.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (passwordText != passwordRepeatText) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            } else {
                createNewPassword()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btnCreateUser.isEnabled = true
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun createNewPassword() {
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