package com.example.threads.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.threads.view.likes_fragments.ActivityFollowingFragment
import com.example.threads.view.likes_fragments.CommentsFragment
import com.example.threads.view.likes_fragments.RequestsFragment

class ActivityFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommentsFragment()
            1 -> RequestsFragment()
            2 -> ActivityFollowingFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}