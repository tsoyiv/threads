package com.example.threads.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.threads.view.followers_fragments.FollowersFragment
import com.example.threads.view.followers_fragments.FollowingFragment
import com.example.threads.view.followers_fragments.PendingFragment

class TabFragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments: List<Fragment> = listOf(
        FollowersFragment(),
        FollowingFragment(),
        PendingFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}