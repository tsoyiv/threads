package com.example.threads.view.log_fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.threads.data.models.UserLogin
import com.example.threads.databinding.FragmentLoginBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.android.synthetic.main.custom_dialog_forgotten_password.view.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()

        etCheck()
        isSuccess()
    }

    private fun etCheck() {
        etEmail = binding.etEmail
        etPassword = binding.etPassword
        btnLogin = binding.btnLogin

        etEmail.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)

        btnLogin.isEnabled = true

        btnLogin.setOnClickListener {
            val emailText = etEmail.text.toString()
            val passwordText = etPassword.text.toString()

            Holder.email = emailText

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(requireContext(), "Both fields must be filled", Toast.LENGTH_SHORT).show()
            } else if (!emailText.contains("@")) {
                Toast.makeText(requireContext(), "Email must contain '@'", Toast.LENGTH_SHORT).show()
            } else {
                loginUser()
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

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        val loginInstance = UserLogin(email, password, false)

        loadingDialogUtil.showLoadingDialog()

        lifecycleScope.launch {
            authViewModel.loginUser(loginInstance)
        }
    }

    private fun isSuccess() {
        authViewModel.loginStatus.observe(viewLifecycleOwner) { isSuccess ->

            loadingDialogUtil.dismissLoadingDialog()

            if (isSuccess) {
                findNavController().navigate(R.id.action_loginFragment_to_tabMainFeedFragment)
            } else {
                callDialog()
            }
        }
    }


    private fun callDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog_forgotten_password, null)

        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        dialogBinding.btnSendEmailLayout.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            myDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun navigation() {
        binding.txtForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.txtReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_regProfileFragment)
        }
        binding.txtReg1.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_regProfileFragment)
        }
    }
}