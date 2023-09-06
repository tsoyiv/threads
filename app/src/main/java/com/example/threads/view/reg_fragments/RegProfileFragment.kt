package com.example.threads.view.reg_fragments

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
import com.example.threads.data.models.CustomUser
import com.example.threads.databinding.FragmentRegProfileBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegProfileFragment : Fragment() {

    private lateinit var binding: FragmentRegProfileBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordRepeat: EditText
    private lateinit var btnCreateUser: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegProfileBinding.inflate(inflater, container, false)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
        checkReg()
        isSuccess()
    }

    private fun checkReg() {
        etEmail = binding.etRegEmail
        etName = binding.etRegName
        etPassword = binding.etPassword
        etPasswordRepeat = binding.etPasswordRepeat
        btnCreateUser = binding.btnCreateUser

        etEmail.addTextChangedListener(textWatcher)
        etName.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
        etPasswordRepeat.addTextChangedListener(textWatcher)

        btnCreateUser.isEnabled = true

        btnCreateUser.setOnClickListener {
            val emailText = etEmail.text.toString()
            val nameText = etName.text.toString()
            val passwordText = etPassword.text.toString()
            val passwordRepeatText = etPasswordRepeat.text.toString()

            if (emailText.isEmpty() || nameText.isEmpty() || passwordText.isEmpty() || passwordRepeatText.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT)
                    .show()
            } else if (!emailText.contains("@")) {
                Toast.makeText(requireContext(), "Email must contain '@'", Toast.LENGTH_SHORT)
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
                registerUser()
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

    private fun isSuccess() {
        authViewModel.registrationStatus.observe(viewLifecycleOwner) { isSuccess ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Check Email to confirm the email",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_regProfileFragment_to_confirmEmailFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error Occurred. Please, try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerUser() {
        val email = binding.etRegEmail.text.toString()
        val username = binding.etRegName.text.toString()
        val password = binding.etPassword.text.toString()
        val confirm_password = binding.etPasswordRepeat.text.toString()

        val userInstance = CustomUser(
            0, email, username, password, confirm_password
        )

        loadingDialogUtil.showLoadingDialog()
        lifecycleScope.launch {
            authViewModel.registerUser(userInstance)
        }
    }

    private fun navigation() {
        binding.btnReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_regProfileFragment_to_loginFragment)
        }
    }
}