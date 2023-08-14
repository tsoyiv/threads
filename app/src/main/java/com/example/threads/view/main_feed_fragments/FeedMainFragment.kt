package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentFeedMainBinding
import com.example.threads.models.FeedItem
import com.example.threads.utils.FeedAdapter

class FeedMainFragment : Fragment() {

    private lateinit var binding: FragmentFeedMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeedMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()

       // navigation()
        setupRV()
    }

    private fun setupRV() {
        val testItems = listOf(
            FeedItem(1, "User1", "This is the first thread."),
            FeedItem(2, "User2", "Testing the second thread."),
            FeedItem(3, "User3", "Third thread for testing."),
            // Add more test items as needed
        )

        recyclerView = binding.rcFeedMainPage
        feedAdapter = FeedAdapter(testItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = feedAdapter
    }

//    private fun navigation() {
//        TODO("Not yet implemented")
//    }
}