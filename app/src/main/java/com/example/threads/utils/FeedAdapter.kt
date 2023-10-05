package com.example.threads.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse
import com.example.threads.utils.adapters.activity.UserThreadAdapter
import com.example.threads.view.main_feed_fragments.TabMainFeedFragmentDirections
import kotlinx.android.synthetic.main.custom_item_view.view.checkBox
import kotlinx.android.synthetic.main.custom_item_view.view.item_view_anotherUser_numbLikes

class FeedAdapter(
    private val threadList: MutableList<ThreadResponse>,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<FeedAdapter.ThreadViewHolder>() {

    interface OnItemClickListener {
       fun likeThreadClick(threadId: Int)
//        fun getUserLikedThread(threadId: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view, parent, false)
        return ThreadViewHolder(view)
    }

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    inner class ThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.item_user_username)
        private val threadContent: TextView = itemView.findViewById(R.id.item_userThread)
        private val likedTextView: TextView =
            itemView.findViewById(R.id.item_view_anotherUser_numbLikes)
        private val imageView: ImageView = itemView.findViewById(R.id.item_image12)
        private val userImage: ImageView = itemView.findViewById(R.id.itemView_user_image)

        fun bind(thread: ThreadResponse) {
            username.text = thread.username
            threadContent.text = thread.content
            val numLikes = thread.likes.toIntOrNull() ?: 0
            likedTextView.text = if (numLikes == 1) {
                "$numLikes like"
            } else {
                "$numLikes likes"
            }
            Glide.with(imageView)
                .load(thread.thread_media)
                .centerCrop()
                .into(imageView)
        }
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val thread = threadList[position]
        holder.bind(thread)

        holder.itemView.setOnClickListener {
            val action =
                TabMainFeedFragmentDirections.actionTabMainFeedFragmentToThreadDescFragment(thread)
            action.thread = thread
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.checkBox.setOnClickListener {
            onItemClickListener?.likeThreadClick(thread.id)
        }

        holder.itemView.item_view_anotherUser_numbLikes.setOnClickListener {
            val action = TabMainFeedFragmentDirections.actionTabMainFeedFragmentToThreadLikedUserFragment(thread)
            action.userRep = thread
            holder.itemView.findNavController().navigate(action)
//            onItemClickListener?.getUserLikedThread(thread.id)
        }
    }

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
