package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.MainActivity
import com.example.threads.databinding.FragmentFeedMainBinding
import com.example.threads.utils.FeedAdapter
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.view_models.ThreadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedMainFragment : Fragment() {

    private lateinit var binding: FragmentFeedMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView
    private val threadViewModel by viewModel<ThreadViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

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
        loadingDialogUtil = LoadingDialogUtil(requireContext())

        isSuccess()
        setupRV()
    }
    private fun isSuccess() {
        loadingDialogUtil.showLoadingDialog()
        threadViewModel.threads.observe(viewLifecycleOwner) { threads ->
            feedAdapter.updateList(threads)
            loadingDialogUtil.dismissLoadingDialog()
        }
        threadViewModel.getThread()
    }

    private fun setupRV() {
        recyclerView = binding.rcFeedMainPage
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = FeedAdapter(mutableListOf(), null) // Initialize with an empty list
        recyclerView.adapter = feedAdapter
    }
}