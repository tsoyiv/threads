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
import com.example.threads.utils.Holder
import com.example.threads.utils.adapters.UserFollowersAdapter
import com.example.threads.view_models.UserDataViewModel
import com.google.android.material.transition.Hold
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var userFollowersAdapter: UserFollowersAdapter
    private lateinit var recyclerView: RecyclerView
    private val userDataViewModel by viewModel<UserDataViewModel>()

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
        fetchUserFollowers()
        checkFetching()
    }

    private fun checkFetching() {
        userDataViewModel.userFollowers.observe(viewLifecycleOwner) { followers ->
            userFollowersAdapter.updateFollowers(followers)
        }
    }

    private fun fetchUserFollowers() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val username = Holder.currentUsername
        userDataViewModel.fetchUserFollowers(authHeader, username)
    }

    private fun setupRV() {
        recyclerView = binding.rcFollowers
        userFollowersAdapter = UserFollowersAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userFollowersAdapter
    }
}