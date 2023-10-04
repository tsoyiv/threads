package com.example.threads.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse
import com.example.threads.models.UserRepresentation
import com.example.threads.utils.UserFollowersDiffCallback
import com.example.threads.view.likes_fragments.CommentsFragmentDirections

class UserFollowersAdapter(private val items: MutableList<UserRepresentation>) :
    RecyclerView.Adapter<UserFollowersAdapter.UserFollowerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_follower_item_view, parent, false)
        return UserFollowerViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserFollowerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

        inner class UserFollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follower: UserRepresentation) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username1)
            val bioTextView = itemView.findViewById<TextView>(R.id.item_userThread1)

            usernameTextView.text = follower.username
            bioTextView.text = follower.name
        }
    }
    fun updateList(newThreads: List<UserRepresentation>) {
        items.clear()
        items.addAll(newThreads)
        notifyDataSetChanged()
    }
}
//class UserFollowersAdapter : RecyclerView.Adapter<UserFollowersAdapter.UserFollowerViewHolder>() {
//
//    private val followers: MutableList<UserRepresentation> = mutableListOf()
//    fun updateFollowers(newFollowers: List<UserRepresentation>) {
//        val diffCallback = UserFollowersDiffCallback(followers, newFollowers)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        followers.clear()
//        followers.addAll(newFollowers)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowerViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_follower_item_view, parent, false)
//        return UserFollowerViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserFollowerViewHolder, position: Int) {
//        val follower = followers[position]
//        holder.bind(follower)
//    }
//
//    override fun getItemCount(): Int = followers.size
//
//    inner class UserFollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(follower: UserRepresentation) {
//            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username1)
//            val bioTextView = itemView.findViewById<TextView>(R.id.item_userThread1)
//
//            usernameTextView.text = follower.username
//            bioTextView.text = follower.name
//        }
//    }
//}