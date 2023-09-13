package com.example.threads.view.followers_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentTabBinding
import com.example.threads.utils.Holder
import com.example.threads.utils.TabFragmentPagerAdapter
import com.example.threads.view_models.UserDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFragment : Fragment(R.layout.fragment_tab) {

    private lateinit var binding: FragmentTabBinding
    private val userDataViewModel by viewModel<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hide()

        setupTab()
        navigation()
        fetchData()
    }

    private fun fetchData() {
        val token = Holder.token
        val authHeader = "Bearer $token"
        userDataViewModel.fetchUserInfo(authHeader)

        userDataViewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.txtFollowPageUsername.text = userInfo.username
            }
        }
    }

    private fun navigation() {
        binding.btnExitFollow.setOnClickListener {
            findNavController().navigate(R.id.action_tabFragment_to_userMainFragment)
        }

        binding.txtSort.setOnClickListener {
            callSortDialog()
        }
    }

    private fun callSortDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.custom_dialog_sort_options, null)

        val defaultRadioButton: RadioButton = bottomSheetView.findViewById(R.id.radBtnDefault)
        val latestRadioButton: RadioButton = bottomSheetView.findViewById(R.id.ratBtnLatest)
        val earliestRadioButton: RadioButton = bottomSheetView.findViewById(R.id.ratBtnEarliest)

        defaultRadioButton.isChecked = true

        defaultRadioButton.setOnClickListener {
            latestRadioButton.isChecked = false
            earliestRadioButton.isChecked = false
        }

        latestRadioButton.setOnClickListener {
            defaultRadioButton.isChecked = false
            earliestRadioButton.isChecked = false
        }

        earliestRadioButton.setOnClickListener {
            defaultRadioButton.isChecked = false
            latestRadioButton.isChecked = false
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun setupTab() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = TabFragmentPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                2 -> "Pending"
                else -> ""
            }
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}