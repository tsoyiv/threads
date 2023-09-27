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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentUserAccountRepresentationBinding
import com.example.threads.models.SearchUserInfo
import com.example.threads.utils.Holder
import com.example.threads.view_models.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountRepresentationFragment : Fragment() {

    private lateinit var binding: FragmentUserAccountRepresentationBinding
    private val userDataViewModel by viewModel<UserDataViewModel>()
    val args: UserAccountRepresentationFragmentArgs by navArgs()

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
        navigation()

        performUserSearch()
    }

    private fun performUserSearch() {
        val userFromArgs: SearchUserInfo? = args.user

        Log.d("UserSearch", "User from Safe Args: $userFromArgs")

        binding.txtNickname.text = args.user.username
        binding.txtUserName.text = args.user.name
        binding.txtUserBio.text = args.user.bio
        binding.txtLink.text = args.user.link

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
    }
}