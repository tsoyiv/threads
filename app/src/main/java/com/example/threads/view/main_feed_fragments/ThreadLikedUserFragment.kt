package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.databinding.FragmentThreadLikedUserBinding
import com.example.threads.utils.FeedAdapter
import com.example.threads.utils.Holder
import com.example.threads.utils.adapters.ThreadUserLikedAdapter
import com.example.threads.view.search_fragments.UserAccountRepresentationFragmentArgs
import com.example.threads.view_models.ThreadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThreadLikedUserFragment : Fragment() {

    private lateinit var binding: FragmentThreadLikedUserBinding
    private lateinit var feedAdapter: ThreadUserLikedAdapter
    private lateinit var recyclerView: RecyclerView
    val args: ThreadLikedUserFragmentArgs by navArgs()
    private val threadViewModel by viewModel<ThreadViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentThreadLikedUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRv()
        fetchLiked()
    }

    private fun fetchLiked() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val id = args.userRep.id
        threadViewModel.likedUsers.observe(viewLifecycleOwner) { response ->
            feedAdapter.updateList(response)
            Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
        }
        threadViewModel.getThreadLikedUsers(authHeader,id)
    }

    private fun setupRv() {
        recyclerView = binding.rcThreadLikedUser
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = ThreadUserLikedAdapter(mutableListOf())
        recyclerView.adapter = feedAdapter
    }
}