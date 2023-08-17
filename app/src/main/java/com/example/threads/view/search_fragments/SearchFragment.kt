package com.example.threads.view.search_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentSearchBinding
import com.example.threads.models.SearchUserInfo
import com.example.threads.utils.SearchUserAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var recyclerView: RecyclerView

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
    }

    private fun setupRV() {
        recyclerView = binding.rcSearchUsers

        val testData = listOf(
            SearchUserInfo(1, "User1", "Bio for User1", 100),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            SearchUserInfo(2, "User2", "Bio for User2", 200),
            // Add more test data here
        )

        searchUserAdapter = SearchUserAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchUserAdapter

        searchUserAdapter.updateSearchUsers(testData)
    }
}