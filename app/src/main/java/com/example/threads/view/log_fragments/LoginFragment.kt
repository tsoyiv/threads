package com.example.threads.view.log_fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
        isSuccess()
        loginUser()
    }

    private fun loginUser() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val loginInstance = UserLogin(email, password, false)

            loadingDialogUtil.showLoadingDialog()

            lifecycleScope.launch {
                authViewModel.loginUser(loginInstance)
            }
        }
    }

    private fun isSuccess() {
        authViewModel.loginStatus.observe(viewLifecycleOwner) { isSuccess ->

            loadingDialogUtil.dismissLoadingDialog()

            if (isSuccess) {
                Toast.makeText(requireContext(), "You are in", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_tabMainFeedFragment)
            } else {
                callDialog()
//                Toast.makeText(
//                    requireContext(),
//                    "Error Occurred. Please, try again",
//                    Toast.LENGTH_SHORT
//                ).show()
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
    }
}