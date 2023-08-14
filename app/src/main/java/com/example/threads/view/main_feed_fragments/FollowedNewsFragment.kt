package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.databinding.FragmentFollowedNewsBinding
import com.example.threads.models.FeedItem
import com.example.threads.models.OnlyFollowersFeedItem
import com.example.threads.utils.FeedAdapter
import com.example.threads.utils.FollowersAdapter

class FollowedNewsFragment : Fragment() {

    private lateinit var binding: FragmentFollowedNewsBinding
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
    }

    private fun setupRV() {
        val testData = listOf(
            OnlyFollowersFeedItem(1, "User1", "Follower of User1"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            OnlyFollowersFeedItem(2, "User2", "Follower of User2"),
            // Add more test data here
        )

        recyclerView = binding.rcFeedMainOnlyFollowers
        followersAdapter = FollowersAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = followersAdapter
    }
}