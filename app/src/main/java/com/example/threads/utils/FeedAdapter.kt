package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse

//class FeedAdapter(private val items: MutableList<ThreadResponse>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view, parent, false)
//        return FeedViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(item: ThreadResponse) {
//            val usernameTextView = itemView.findViewById<TextView>(R.id.item_user_username)
//            val threadTextView = itemView.findViewById<TextView>(R.id.item_userThread)
//            usernameTextView.text = item.author
//            threadTextView.text = item.content
//            usernameTextView.text = "Test Username"
//            threadTextView.text = "This is a test thread."
//        }
//    }
//
//    fun updateList(newThreads: List<ThreadResponse>) {
//        items.clear()
//        items.addAll(newThreads)
//        notifyDataSetChanged()
//    }
//}

class FeedAdapter(
    private val threadList: MutableList<ThreadResponse>,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<FeedAdapter.ThreadViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(thread: ThreadResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view, parent, false)
        return ThreadViewHolder(view)
    }

    inner class ThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.item_user_username)
        private val threadContent: TextView = itemView.findViewById(R.id.item_userThread)
        private val likedTextView: TextView =
            itemView.findViewById(R.id.item_view_anotherUser_numbLikes)
        //private val imageView: ImageView = itemView.findViewById(R.id.image_on_item)

        fun bind(thread: ThreadResponse) {
            username.text = thread.author
            threadContent.text = thread.content
            val numLikes = thread.likes.toIntOrNull() ?: 0
            likedTextView.text = if (numLikes == 1) {
                "$numLikes like"
            } else {
                "$numLikes likes"
            }
//            if (!thread.thread_media.isNullOrEmpty()) {
//                Glide.with(imageView)
//                    .load(product.photo)
//                    .centerCrop()
//                    .into(imageView)
//            }
        }
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val thread = threadList[position]
        holder.bind(thread)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val thread = threadList[position]
//
//        holder.itemView.apply {
//            item_user_username.text = thread.author
//            item_userThread.text = thread.content
//            item_view_anotherUser_numbLikes.text = thread.likes
//
//            setOnClickListener {
//                onItemClickListener?.onItemClick(thread)
//            }
//        }
//    }

    override fun getItemCount(): Int {
        return threadList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateList(newThreads: List<ThreadResponse>) {
        threadList.clear()
        threadList.addAll(newThreads)
        notifyDataSetChanged()
    }


}
