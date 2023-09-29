package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import com.example.threads.databinding.FragmentThreadDescBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.utils.adapters.activity.ThreadCommentAdapter
import com.example.threads.view_models.ThreadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThreadDescFragment : Fragment() {

    val args: ThreadDescFragmentArgs by navArgs()
    private lateinit var thread: ThreadResponse
    private lateinit var binding: FragmentThreadDescBinding
    private val threadViewModel by viewModel<ThreadViewModel>()
    private lateinit var commentAdapter: ThreadCommentAdapter
    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentThreadDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        thread = args.thread

        loadDetailPage()
        navigation()
        writeComment()
        isCommentSuccess()
        RVsetup()
        updateComments(thread.id)
    }

    private fun updateComments(threadId: Int) {
        val token = Holder.token
        val authHeader = "Bearer $token"
        threadViewModel.fetchThreadsWithComments(authHeader, threadId)
        loadingDialogUtil.showLoadingDialog()
        threadViewModel.threadsWithComments.observe(viewLifecycleOwner) { threadsWithComments ->
            val commentsForSelectedThread = threadsWithComments
                .filter { it.id == threadId }
                .flatMap { it.comments }

            commentAdapter.updateComments(commentsForSelectedThread)
            loadingDialogUtil.dismissLoadingDialog()
        }
    }

//    private fun updateComments() {
//        val token = Holder.token
//        val authHeader = "Bearer $token"
//        threadViewModel.fetchThreadsWithComments(authHeader, thread.id)
//        loadingDialogUtil.showLoadingDialog()
//        threadViewModel.threadsWithComments.observe(viewLifecycleOwner) { threadsWithComments ->
//            val allComments = threadsWithComments.flatMap { it.comments }
//            commentAdapter.updateComments(allComments)
//            loadingDialogUtil.dismissLoadingDialog()
//        }
//    }

    private fun RVsetup() {
        commentAdapter = ThreadCommentAdapter()
        val recyclerView = binding.rcComments
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentAdapter
    }

    private fun writeComment() {
        binding.btnSendComment.setOnClickListener {
            val token = Holder.token
            val authHolder = "Bearer $token"
            thread = args.thread
            val content = binding.etLeftComment.text.toString()

            if (content.isNotEmpty()) {
                threadViewModel.writeComment(authHolder, thread.id, content)
                binding.etLeftComment.text?.clear()
                updateComments(thread.id)
            } else {
                Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isCommentSuccess() {
        threadViewModel.commentResult.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(requireContext(), "Comment added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Comment were not added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigation() {
        binding.btnExitFromDetailPage.setOnClickListener {
            findNavController().navigate(R.id.action_threadDescFragment_to_tabMainFeedFragment2)
        }
    }

    private fun loadDetailPage() {
        thread = args.thread
        val token = Holder.token
        val authHolder = "Bearer $token"
        descriptionUi(thread)
        getThread(authHolder, thread.id)
        loadImage(thread.thread_media)
    }

    private fun getThread(token: String, id: Int) {
        threadViewModel.threadDetails.observe(viewLifecycleOwner) { threadResponse ->
            if (threadResponse != null) {
                descriptionUi(threadResponse)
                loadImage(threadResponse.thread_media)
            }
        }
        threadViewModel.getThreadDetails(token, id)
    }

    private fun descriptionUi(product: ThreadResponse) {
        binding.itemUserUsername.text = product.username
        binding.itemUserThread.text = product.content

        val fetchUsername = product.author
        binding.etLeftComment.hint = "Reply to $fetchUsername"
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(requireContext())
            .load(imageUrl)
            .centerCrop()
            .into(binding.itemViewUserImage)
    }
}