package com.example.threads.view.likes_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.databinding.FragmentRequestsBinding
import com.example.threads.models.ActivityFollowing
import com.example.threads.models.ActivityRequest
import com.example.threads.utils.adapters.activity.FollowingAdapter
import com.example.threads.utils.adapters.activity.RequestAdapter

class RequestsFragment : Fragment() {

    private lateinit var binding: FragmentRequestsBinding
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
    }

    private fun setupRV() {
        val testData = listOf(
            ActivityRequest(1, "Vlad11", "12m"),
            ActivityRequest(1, "user", "12m"),
            ActivityRequest(1, "hello", "12m"),
            ActivityRequest(1, "hi", "12m"),
            ActivityRequest(1, "neobis", "12m"),
            ActivityRequest(1, "VladTsoi", "12m")
        )

        recyclerView = binding.rcActivityRequests
        requestAdapter = RequestAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = requestAdapter

        requestAdapter.setObserver(recyclerView)

        requestAdapter.setItemCountListener(object : RequestAdapter.ItemCountListener {
            override fun onItemCountChanged(itemCount: Int) {
                val textView = binding.txtNumbRequests
                val numRequestsText = resources.getString(R.string.num_requests_text, itemCount)
                textView.text = numRequestsText
            }
        })
    }
}