package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.models.UserRepresentation

class UserFollowersAdapter : RecyclerView.Adapter<UserFollowersAdapter.UserFollowerViewHolder>() {

    private val followers: MutableList<UserRepresentation> = mutableListOf()

    fun updateFollowers(newFollowers: List<UserRepresentation>) {
        val diffCallback = UserFollowersDiffCallback(followers, newFollowers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        followers.clear()
        followers.addAll(newFollowers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_follower_item_view, parent, false)
        return UserFollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserFollowerViewHolder, position: Int) {
        val follower = followers[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int = followers.size

    inner class UserFollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follower: UserRepresentation) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
            val bioTextView = itemView.findViewById<TextView>(R.id.item_userThread)

            usernameTextView.text = follower.username
            bioTextView.text = follower.bio
        }
    }
}