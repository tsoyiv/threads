package com.example.threads.view.reg_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentRegProfileBinding

class RegProfileFragment : Fragment() {

    private lateinit var binding: FragmentRegProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        navigation()
    }

    private fun navigation() {
        binding.btnReturnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_regProfileFragment_to_loginFragment)
        }
    }
}