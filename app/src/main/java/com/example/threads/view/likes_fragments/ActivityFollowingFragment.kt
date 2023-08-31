package com.example.threads.view.likes_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.databinding.FragmentActivityFollowingBinding
import com.example.threads.models.ActivityFollowing
import com.example.threads.models.OnlyFollowersFeedItem
import com.example.threads.utils.FollowersAdapter
import com.example.threads.utils.adapters.activity.FollowingAdapter

class ActivityFollowingFragment : Fragment() {

    private lateinit var binding: FragmentActivityFollowingBinding
    private lateinit var followersAdapter: FollowingAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentActivityFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
    }

    private fun setupRV() {
        val testData = listOf(
            ActivityFollowing(1, "Vlad11", "12m", "Followed you"),
            ActivityFollowing(1, "user", "12m", "Followed you"),
            ActivityFollowing(1, "hello", "12m", "Followed you"),
            ActivityFollowing(1, "hi", "12m", "Followed you"),
            ActivityFollowing(1, "neobis", "12m", "Followed you"),
            ActivityFollowing(1, "VladTsoi", "12m", "Followed you")
        )

        recyclerView = binding.rcActivityFollowing
        followersAdapter = FollowingAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = followersAdapter
    }
}