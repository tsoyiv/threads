package com.example.threads.view.reg_fragments

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
import com.example.threads.data.models.CustomUser
import com.example.threads.databinding.FragmentRegProfileBinding
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegProfileFragment : Fragment() {

    private lateinit var binding: FragmentRegProfileBinding
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

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
        registerUser()
        isRegSuccess()
    }

    private fun isRegSuccess() {
        authViewModel.registrationStatus.observe(viewLifecycleOwner) { isSuccess ->
            loadingDialogUtil.dismissLoadingDialog()
            if (isSuccess) {
                Toast.makeText(requireContext(), "User Created", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_regProfileFragment_to_loginFragment)
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
        binding.btnCreateUser.setOnClickListener {
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
    }

    private fun navigation() {
        binding.btnReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_regProfileFragment_to_loginFragment)
        }
    }
}