package com.example.threads.view.user_fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentUserMainBinding
import kotlinx.android.synthetic.main.custom_dialog_logout.view.*

class UserMainFragment : Fragment() {

    private lateinit var binding: FragmentUserMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()

        logOut()
        navigation()
    }

    private fun navigation() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_editProfileFragment)
        }
    }

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            callDialog()
        }
    }

    private fun callDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog_logout, null)

        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        dialogBinding.txt_logout.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_loginFragment)
            myDialog.dismiss()
        }
        dialogBinding.txt_cancel.setOnClickListener {
            myDialog.dismiss()
        }
    }
}