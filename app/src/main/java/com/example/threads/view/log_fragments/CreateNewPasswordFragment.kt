package com.example.threads.view.log_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.threads.R
import com.example.threads.databinding.FragmentCreatePasswordBinding

class CreateNewPasswordFragment : Fragment() {

    private lateinit var binding: FragmentCreatePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation()
    }

    private fun navigation() {
        binding.btnReturnToResetPage.setOnClickListener {
            findNavController().navigate(R.id.action_createPasswordFragment_to_forgotPasswordFragment)
        }
    }
}