package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threads.R
import com.example.threads.models.SearchUserInfo
import com.example.threads.view.main_feed_fragments.TabMainFeedFragmentDirections
import com.example.threads.view.search_fragments.SearchFragmentDirections
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.fragment_search.view.searchUsers

class SearchUserAdapter : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    private val searchUserList: MutableList<SearchUserInfo> = mutableListOf()

    fun updateSearchUsers(newSearchUsers: List<SearchUserInfo>) {
        val diffCallback = SearchUserDiffCallback(searchUserList, newSearchUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        searchUserList.clear()
        searchUserList.addAll(newSearchUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_search_for_user_item, parent, false)
        return SearchUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val searchUser = searchUserList[position]
        holder.bind(searchUser)

        holder.itemView.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToUserAccountRepresentationFragment(searchUser)
            action.user = searchUser
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = searchUserList.size

    inner class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchUser: SearchUserInfo) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
            val bioTextView = itemView.findViewById<TextView>(R.id.item_userSearchBio)
            val followersTextView = itemView.findViewById<TextView>(R.id.txt_numbOfFollowers)
            val profileImageView = itemView.findViewById<ShapeableImageView>(R.id.itemView_user_image)
            val btnFollow = itemView.findViewById<AppCompatButton>(R.id.centerButton)
            val isFollow = searchUser.is_following
            if (isFollow) {
                btnFollow.text = "Following"
            } else {
                btnFollow.text = "Follow"
            }

            usernameTextView.text = searchUser.username
            bioTextView.text = searchUser.bio
            val formattedCount = formatFollowerCount(searchUser.numbOfFollowers)
            followersTextView.text = formattedCount

            Glide.with(itemView)
                .load(searchUser.profile_picture)
                .into(profileImageView)
        }
    }

    fun clearSearchUsers() {
        searchUserList.clear()
        notifyDataSetChanged()
    }
    private fun formatFollowerCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> (count / 1000.0).toString() + "k"
            else -> (count / 1000).toString() + "k"
        }
    }

}