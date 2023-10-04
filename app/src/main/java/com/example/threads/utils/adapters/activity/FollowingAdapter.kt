package com.example.threads.utils.adapters.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.models.ActivityFollowing
import com.example.threads.models.OnlyFollowersFeedItem
import com.example.threads.utils.FollowersAdapter

class FollowingAdapter(private val items: List<ActivityFollowing>) :
    RecyclerView.Adapter<FollowingAdapter.FollowerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_activity_following_item, parent, false)
        return FollowerViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ActivityFollowing) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.txtActivityFollowingUsername)
            val name = itemView.findViewById<TextView>(R.id.txtActivityFollowingState)
            val time = itemView.findViewById<TextView>(R.id.txtDateOfFollowed)

            usernameTextView.text = item.username
        }
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}