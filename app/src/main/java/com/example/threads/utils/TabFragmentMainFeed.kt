package com.example.threads.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.threads.view.main_feed_fragments.FeedMainFragment
import com.example.threads.view.main_feed_fragments.FollowedNewsFragment

class TabFragmentMainFeed(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments: List<Fragment> = listOf(
        FeedMainFragment(),
        FollowedNewsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}