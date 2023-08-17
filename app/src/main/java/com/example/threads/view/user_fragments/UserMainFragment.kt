package com.example.threads.view.user_fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentUserMainBinding
import com.example.threads.models.FeedItem
import com.example.threads.utils.FeedAdapter
import kotlinx.android.synthetic.main.custom_dialog_logout.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class UserMainFragment : Fragment() {

    private lateinit var binding: FragmentUserMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView
    private val feedItems: MutableList<FeedItem> = mutableListOf()

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
        setupRV()
    }

    private fun setupRV() {

        val testItems = listOf(
            FeedItem(1, "User1", "This is the first thread."),
            FeedItem(2, "User2", "Testing the second thread."),
            FeedItem(3, "User3", "Third thread for testing."),
            // Add more test items as needed
        )

        recyclerView = binding.rcFeedUserPage
        feedAdapter = FeedAdapter(testItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = feedAdapter
    }

    private fun navigation() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_editProfileFragment)
        }

        binding.txtNumbFollowers.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_tabFragment)
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