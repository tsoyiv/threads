package com.example.threads.view.search_fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.data.models.SubscribeRequest
import com.example.threads.databinding.FragmentUserAccountRepresentationBinding
import com.example.threads.models.FeedItem
import com.example.threads.models.SearchUserInfo
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.utils.adapters.activity.UserThreadAdapter
import com.example.threads.view_models.ThreadViewModel
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountRepresentationFragment : Fragment() {

    private lateinit var binding: FragmentUserAccountRepresentationBinding
    private val userDataViewModel by viewModel<UserDataViewModel>()
    val args: UserAccountRepresentationFragmentArgs by navArgs()
    private lateinit var feedAdapter: UserThreadAdapter
    private lateinit var recyclerView: RecyclerView
    private val threadViewModel by viewModel<ThreadViewModel>()
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private var isFollowing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserAccountRepresentationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()
        loadingDialogUtil = LoadingDialogUtil(requireContext())

        navigation()
        performUserSearch()
        setupRV()
        getUserThread()
//        followUser()
        // Setup the followResult observer here (outside the click listener)
        userDataViewModel.followResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                updateButtonState()
                Toast.makeText(requireContext(), "Followed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
        updateButtonState()
        binding.btnSubscribe.setOnClickListener {
            isFollowing = !isFollowing

            val token = Holder.token
            val authHeader = "Bearer $token"
            val id = args.user.id

            val requestType = if (isFollowing) "follow" else "unfollow"
            val request = SubscribeRequest(id.toString(), requestType)
            userDataViewModel.followUser(authHeader, request)
        }

    }
    private fun updateButtonState() {
        if (isFollowing) {
            binding.btnSubscribe.text = "Followed"
            binding.btnSubscribe.setBackgroundResource(R.drawable.btn_google)
            binding.btnSubscribe.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            binding.btnSubscribe.text = "Follow"
            binding.btnSubscribe.setBackgroundResource(R.drawable.custom_btn_follow)
            binding.btnSubscribe.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }
//    private var isFollowing = false
//    private fun followUser() {
//        val token = Holder.token
//        val authHeader = "Bearer $token"
//        val id = args.user.id
//
//        binding.btnSubscribe.setOnClickListener {
//            if (isFollowing) {
//                val unfollowInstance = SubscribeRequest(id.toString(), "unfollow")
//                userDataViewModel.followUser(authHeader, unfollowInstance)
//            } else {
//                val followInstance = SubscribeRequest(id.toString(), "follow")
//                userDataViewModel.followUser(authHeader, followInstance)
//            }
//
//            userDataViewModel.followResult.observe(viewLifecycleOwner) { response ->
//                if (response != null) {
//                    isFollowing = !isFollowing
//                    if (isFollowing) {
//                        binding.btnSubscribe.text = "followed"
//                        binding.btnSubscribe.setBackgroundResource(R.drawable.btn_google)
//                        binding.btnSubscribe.setTextColor(
//                            ContextCompat.getColor(
//                                requireContext(),
//                                R.color.black
//                            )
//                        )
//                    }else {
//                        binding.btnSubscribe.text = "Follow"
//                        binding.btnSubscribe.setBackgroundResource(R.drawable.custom_btn_follow)
//                        binding.btnSubscribe.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//                    }
//                    Toast.makeText(requireContext(), "Followed", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }


    private fun getUserThread() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val email = args.user.email

        loadingDialogUtil.showLoadingDialog()
        threadViewModel.threads.observe(viewLifecycleOwner) { threads ->
            feedAdapter.updateList(threads)
            loadingDialogUtil.dismissLoadingDialog()
        }
        threadViewModel.getUserThread(authHeader, email)
    }

    private fun setupRV() {
        recyclerView = binding.rcUsersFeed
        val userEmail = args.user.email
        feedAdapter = UserThreadAdapter(
            mutableListOf(), userEmail,
            null
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = feedAdapter
    }

    private fun performUserSearch() {
        val userFromArgs: SearchUserInfo? = args.user

        Log.d("UserSearch", "User from Safe Args: $userFromArgs")

        binding.txtNickname.text = args.user.username
        binding.txtUserName.text = args.user.name
        binding.txtUserBio.text = args.user.bio
        binding.txtLink.text = args.user.link
        Holder.currentUsername = args.user.username
        val isPrivateAccount = args.user.is_private

        if (!isPrivateAccount) {
            binding.privateLayout.visibility = View.GONE
        } else {
            binding.txtUserNumbFollowers.isEnabled = false
        }

        val profilePictureUrl: String? = args.user.profile_picture
        val imgUser = binding.imgUser

        Glide.with(this)
            .load(profilePictureUrl)
            .error(R.drawable.img_userphoto)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imgUser)

        val ownPageCheckEmail = args.user.email
        val ownEmail = Holder.email
        if (ownPageCheckEmail == ownEmail) {
            findNavController().navigate(R.id.userMainFragment)
        }

        val repostedText = args.user.name
        val correctText = "$repostedText reposted"
        binding.txtUserRepostedThread.text = correctText

        val numberOfFollowers = args.user.numbOfFollowers
        val followersText = if (numberOfFollowers == 1) {
            "$numberOfFollowers follower"
        } else {
            "$numberOfFollowers followers"
        }
        binding.txtUserNumbFollowers.text = followersText


        userDataViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            val userToDisplay = userFromArgs ?: searchResults.firstOrNull()
            if (userToDisplay != null) {
                binding.txtUserName.text = userToDisplay.username
            }
            Log.d("UserSearch1", "User from Safe Args: $userFromArgs")
        }

        userDataViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigation() {
        binding.btnBackFromUserAccount.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountRepresentationFragment_to_searchFragment)
        }
        binding.txtUserNumbFollowers.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountRepresentationFragment_to_tabFragment2)
        }
    }
}