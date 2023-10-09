package com.example.threads.view.followers_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentFollowingBinding
import com.example.threads.utils.FollowersAdapter
import com.example.threads.utils.Holder
import com.example.threads.utils.adapters.UserFollowersAdapter
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var userFollowersAdapter: UserFollowersAdapter
    private lateinit var recyclerView: RecyclerView
    private val userDataViewModel by viewModel<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        setupRV()
        checkFetching()
    }

    private fun checkFetching() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val username = Holder.currentUsername
        userDataViewModel.userFollowers.observe(viewLifecycleOwner) {
            userFollowersAdapter.updateList(it.following)
        }
        userDataViewModel.fetchUserFollowers(authHeader, username)
    }

    private fun setupRV() {
        recyclerView = binding.rcFollowing
        userFollowersAdapter = UserFollowersAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userFollowersAdapter
    }
}