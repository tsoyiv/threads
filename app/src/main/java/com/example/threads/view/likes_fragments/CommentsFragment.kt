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
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.utils.adapters.activity.CommentsAdapter
import com.example.threads.utils.adapters.activity.RequestAdapter
import com.example.threads.utils.adapters.activity.ThreadCommentAdapter
import com.example.threads.view_models.ThreadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private val threadViewModel by viewModel<ThreadViewModel>()

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
        loadingDialogUtil = LoadingDialogUtil(requireContext())

        setupRV()
        updateComments()
    }

    private fun updateComments() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val email = Holder.email

        threadViewModel.getThreadsWithCommentsActivity(authHeader, email)
        loadingDialogUtil.showLoadingDialog()

        threadViewModel.threadsLiveData.observe(viewLifecycleOwner) { threadsWithComments ->
            val commentsForSelectedThread = threadsWithComments
                .flatMap { it.comments }
            commentsAdapter.updateComments(commentsForSelectedThread)
            loadingDialogUtil.dismissLoadingDialog()
        }
    }

    private fun setupRV() {
        recyclerView = binding.rcActivityComments
        commentsAdapter = CommentsAdapter(null)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentsAdapter

    }
}