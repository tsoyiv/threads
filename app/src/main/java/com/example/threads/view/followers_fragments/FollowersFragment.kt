package com.example.threads.view.followers_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.databinding.FragmentFollowersBinding
import com.example.threads.models.UserRepresentation
import com.example.threads.utils.UserFollowersAdapter

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var userFollowersAdapter: UserFollowersAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
    }

    private fun setupRV() {
        recyclerView = binding.rcFollowers
        userFollowersAdapter = UserFollowersAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userFollowersAdapter

        val testData = listOf(
            UserRepresentation(1, "User1", "Bio for User1"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
            UserRepresentation(2, "User2", "Bio for User2"),
        )
        userFollowersAdapter.updateFollowers(testData)

    }
}