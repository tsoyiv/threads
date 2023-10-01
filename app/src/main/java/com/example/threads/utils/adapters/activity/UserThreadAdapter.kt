package com.example.threads.utils.adapters.activity

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
import com.example.threads.utils.Holder
import com.example.threads.view.main_feed_fragments.TabMainFeedFragmentDirections
import com.example.threads.view.user_fragments.UserMainFragmentDirections

class UserThreadAdapter(
    private val threadList: MutableList<ThreadResponse>,
    private val userEmail: String,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<UserThreadAdapter.ThreadViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(thread: ThreadResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view_owner, parent, false)
        return ThreadViewHolder(view)
    }

    private var clickListener: ((ThreadResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (ThreadResponse) -> Unit) {
        clickListener = listener
    }

    inner class ThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.item_owner_username)
        private val threadContent: TextView = itemView.findViewById(R.id.item_ownerThread)
        private val likedTextView: TextView =
            itemView.findViewById(R.id.item_view_ownUser_numbLikes)
        private val imageView: ImageView = itemView.findViewById(R.id.itemView_owner_image)
        private val removeImage: ImageView = itemView.findViewById(R.id.btn_remove_own_thread)
        val ownEmail = Holder.email

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


            if (userEmail != ownEmail) {
                removeImage.visibility = View.GONE
            } else {
                removeImage.visibility = View.VISIBLE
            }
        }
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val thread = threadList[position]
        holder.bind(thread)

        holder.itemView.setOnClickListener {
            val action =
                UserMainFragmentDirections.actionUserMainFragmentToThreadDescFragment(thread)
            action.thread = thread
            holder.itemView.findNavController().navigate(action)
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