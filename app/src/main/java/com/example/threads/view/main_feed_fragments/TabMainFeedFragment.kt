package com.example.threads.view.main_feed_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.threads.MainActivity
import com.example.threads.R
import com.example.threads.databinding.FragmentTabMainFeedBinding
import com.example.threads.utils.FeedAdapter
import com.example.threads.utils.TabFragmentMainFeed
import com.example.threads.utils.TabFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TabMainFeedFragment : Fragment() {

    private lateinit var binding: FragmentTabMainFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTabMainFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBtm()

        setupTab()
    }

    private fun setupTab() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = TabFragmentMainFeed(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "For you"
                1 -> "Following"
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