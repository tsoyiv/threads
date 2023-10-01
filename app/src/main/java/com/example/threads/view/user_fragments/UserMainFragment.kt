package com.example.threads.view.user_fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse
import com.example.threads.databinding.FragmentUserMainBinding
import com.example.threads.models.FeedItem
import com.example.threads.utils.FeedAdapter
import com.example.threads.utils.Holder
import com.example.threads.utils.LoadingDialogUtil
import com.example.threads.utils.adapters.activity.UserThreadAdapter
import com.example.threads.view_models.ThreadViewModel
import com.example.threads.view_models.UserDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.btm_dialog_share.*
import kotlinx.android.synthetic.main.custom_dialog_logout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserMainFragment : Fragment() {

    private lateinit var binding: FragmentUserMainBinding
    private lateinit var feedAdapter: UserThreadAdapter
    private lateinit var recyclerView: RecyclerView
    private val userDataViewModel by viewModel<UserDataViewModel>()
    private val threadViewModel by viewModel<ThreadViewModel>()
    private lateinit var textLink: String
    private val itemList: MutableList<FeedItem> = mutableListOf()
    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()
        loadingDialogUtil = LoadingDialogUtil(requireContext())

        logOut()
        navigation()
        setupRV()
        fetchData()
        openLink()
        getUserThread()
    }

    private fun removeThread(id: Int) {
        val token = Holder.token
        val authHeader = "Bearer $token"

        loadingDialogUtil.showLoadingDialog()
        threadViewModel.removeThreadResult.observe(viewLifecycleOwner) { success->
            if (success) {
                Toast.makeText(requireContext(), "removed", Toast.LENGTH_SHORT).show()
                loadingDialogUtil.dismissLoadingDialog()
                getUserThread()
            } else {
            }
        }
        threadViewModel.removeThread(authHeader, id)
    }

    private fun getUserThread() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        val email = Holder.email

        loadingDialogUtil.showLoadingDialog()
        threadViewModel.threads.observe(viewLifecycleOwner) { threads ->
            feedAdapter.updateList(threads)
            loadingDialogUtil.dismissLoadingDialog()
        }
        threadViewModel.getUserThread(authHeader, email)
    }

    private fun openLink() {
        binding.txtLink.setOnClickListener {
            val uri = Uri.parse(textLink)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun fetchData() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        userDataViewModel.fetchUserInfo(authHeader)

        userDataViewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.txtUsername.text = userInfo.username
                Holder.currentUsername = userInfo.username
                binding.txtName.text = userInfo.name
                binding.txtName.text = userInfo.name
                binding.txtUserBio.text = userInfo.bio
                binding.txtLink.text = userInfo.link
                textLink = userInfo.link.toString()

                userInfo.profile_picture?.let { uriString ->
                    val profilePictureUri = Uri.parse(uriString)
                    Glide.with(requireContext())
                        .load(profilePictureUri)
                        .into(binding.imgUserImageMainPage)
                }
            }
        }
    }

    private fun shareProfileBottom() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.btm_dialog_share, null)

        val copyLink = bottomSheetView.findViewById<LinearLayout>(R.id.btn_copyLink)
        val shareVia = bottomSheetView.findViewById<LinearLayout>(R.id.btn_shareVia)

        copyLink.setOnClickListener {
            Toast.makeText(requireContext(), "top", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }
        shareVia.setOnClickListener {
            Toast.makeText(requireContext(), "bottom", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun setupRV() {
        recyclerView = binding.rcFeedUserPage
        feedAdapter = UserThreadAdapter(
            mutableListOf(),
            Holder.email,
            listener
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = feedAdapter
//        feedAdapter.setOnItemClickListener(object : UserThreadAdapter.OnItemClickListener {
//            override fun deleteThread(threadId: Int) {
//                removeThread(threadId)
//            }
//        })
    }

    private val listener = object : UserThreadAdapter.OnItemClickListener {
        override fun deleteThread(threadId: Int) {
            removeThread(threadId)
        }
    }

    private fun navigation() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_editProfileFragment)
        }

        binding.txtNumbFollowers.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_tabFragment)
        }

        binding.btnShareProfile.setOnClickListener {
            shareProfileBottom()
        }
    }

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            callDialog()
        }
    }

    private fun callDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog_logout, null)

        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        dialogBinding.txt_logout.setOnClickListener {
            findNavController().navigate(R.id.action_userMainFragment_to_loginFragment)
            myDialog.dismiss()
        }
        dialogBinding.txt_cancel.setOnClickListener {
            myDialog.dismiss()
        }
    }
}