package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threads.R
import com.example.threads.models.SearchUserInfo
import com.google.android.material.imageview.ShapeableImageView

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
    }

    override fun getItemCount(): Int = searchUserList.size

    inner class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchUser: SearchUserInfo) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
            val bioTextView = itemView.findViewById<TextView>(R.id.item_userSearchBio)
            val followersTextView = itemView.findViewById<TextView>(R.id.txt_numbOfFollowers)
            val profileImageView = itemView.findViewById<ShapeableImageView>(R.id.itemView_user_image)

            usernameTextView.text = searchUser.username
            bioTextView.text = searchUser.bio
            followersTextView.text = searchUser.numbOfFollowers.toString()

            Glide.with(itemView)
                .load(searchUser.profile_picture)
                .into(profileImageView)
        }
    }
}