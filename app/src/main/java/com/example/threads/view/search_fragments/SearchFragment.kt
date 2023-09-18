package com.example.threads.view.search_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentSearchBinding
import com.example.threads.models.SearchUserInfo
import com.example.threads.utils.Holder
import com.example.threads.utils.SearchUserAdapter
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private val userDataViewModel by viewModel<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()

        setupRV()
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView = binding.searchUsers
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    performUserSearch(newText)
                } else {
                    searchUserAdapter.updateSearchUsers(emptyList())
                }
                return true
            }
        })
    }

    private fun performUserSearch(query: String) {
        val token = Holder.token
        val authHeader = "Bearer $token"
        userDataViewModel.searchUsers(authHeader, query)

        userDataViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            searchUserAdapter.updateSearchUsers(searchResults)
        }

        userDataViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRV() {
        recyclerView = binding.rcSearchUsers
        searchUserAdapter = SearchUserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchUserAdapter
//        recyclerView = binding.rcSearchUsers
//
//        val testData = listOf(
//            SearchUserInfo(1, "User1", "Bio for User1", 100),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            SearchUserInfo(2, "User2", "Bio for User2", 200),
//            // Add more test data here
//        )
//
//        searchUserAdapter = SearchUserAdapter()
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = searchUserAdapter
//
//        searchUserAdapter.updateSearchUsers(testData)
    }
}