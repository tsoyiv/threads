package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.models.OnlyFollowersFeedItem

class FollowersAdapter(private val items: List<OnlyFollowersFeedItem>) :
    RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: OnlyFollowersFeedItem) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
            val threadTextView = itemView.findViewById<TextView>(R.id.item_userThread)

            usernameTextView.text = item.username
            threadTextView.text = item.thread
        }
    }
}

