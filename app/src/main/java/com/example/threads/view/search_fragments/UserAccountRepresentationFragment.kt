package com.example.threads.view.search_fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentUserAccountRepresentationBinding
import com.example.threads.models.SearchUserInfo
import com.example.threads.utils.Holder
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountRepresentationFragment : Fragment() {

    private lateinit var binding: FragmentUserAccountRepresentationBinding
    private val userDataViewModel by viewModel<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserAccountRepresentationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()
        navigation()

        performUserSearch()
    }

    private fun performUserSearch() {
        userDataViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            searchResults?.let { data ->
                if (data.isNotEmpty()) {
                    val firstResult = data[0]
                    binding.txtUserName.text = firstResult.username
                } else {
                    binding.txtUserName.text = "No results found"
                }
            }
        }

        userDataViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigation() {
        binding.btnBackFromUserAccount.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountRepresentationFragment_to_searchFragment)
        }
    }
}