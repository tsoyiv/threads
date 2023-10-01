package com.example.threads.utils.adapters.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse
import com.example.threads.models.ActivityComments
import com.example.threads.view.likes_fragments.CommentsFragmentDirections
import com.example.threads.view.main_feed_fragments.TabMainFeedFragmentDirections

class CommentsAdapter(private val items: MutableList<ThreadResponse>) :
    RecyclerView.Adapter<CommentsAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_activity_comments, parent, false)
        return FollowerViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val action =
                CommentsFragmentDirections.actionCommentsFragmentToThreadDescFragment2(item)
            action.thread = item
            holder.itemView.findNavController().navigate(action)
        }
    }

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ThreadResponse) {
            val username = itemView.findViewById<TextView>(R.id.item_user_username1)
            val thread = itemView.findViewById<TextView>(R.id.txtThread)
            val comment = itemView.findViewById<TextView>(R.id.txtComment)
            val time = itemView.findViewById<TextView>(R.id.txtTimeFollowed)

            username.text = item.username
//            time.text = item.time

            val fullThread = item.content
            val maxWordsToShow = 5
            val words = fullThread.split(" ")
            val truncatedThread = if (words.size > maxWordsToShow) {
                words.take(maxWordsToShow).joinToString(" ") + "..."
            } else {
                fullThread
            }
            thread.text = truncatedThread

//            val fullComment = item.comment
//            val truncatedComment = if (words.size > maxWordsToShow) {
//                words.take(maxWordsToShow).joinToString(" ") + "..."
//            } else {
//                fullComment
//            }
//            comment.text = truncatedComment
        }
    }
    fun updateList(newThreads: List<ThreadResponse>) {
        items.clear()
        items.addAll(newThreads)
        notifyDataSetChanged()
    }
}
