package com.example.threads.view.likes_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.databinding.FragmentCommentsBinding
import com.example.threads.models.ActivityComments
import com.example.threads.models.ActivityRequest
import com.example.threads.utils.adapters.activity.CommentsAdapter
import com.example.threads.utils.adapters.activity.RequestAdapter

class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()
    }

    private fun setupRV() {
        val testData = listOf(
            ActivityComments(1, "Vlad11", "12m", "today i gonna buy a new home", "okay, that's a great idea for you"),
            ActivityComments(1, "user", "12m", "today i gonna buy a new home", "okay, that's a great idea for you"),
            ActivityComments(1, "hello", "12m", "today i gonna buy a new home", "okay, that's a great idea for you"),
            ActivityComments(1, "hi", "12m", "today i gonna buy a new home", "hi hello hello hello hello hello"),
            ActivityComments(1, "neobis", "12m", "today i gonna buy a new home", "hi hello hello hello hello hello"),
            ActivityComments(1, "VladTsoi", "12m", "today i gonna buy a new home", "hi hello hello hello hello hello")
        )

        recyclerView = binding.rcActivityComments
        commentsAdapter = CommentsAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentsAdapter

    }
}