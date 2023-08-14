package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.models.FeedItem

class FeedAdapter(private val items: List<FeedItem>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeedItem) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
            val threadTextView = itemView.findViewById<TextView>(R.id.item_userThread)
            usernameTextView.text = item.username
            threadTextView.text = item.thread
            usernameTextView.text = "Test Username"
            threadTextView.text = "This is a test thread."
        }
    }
}
