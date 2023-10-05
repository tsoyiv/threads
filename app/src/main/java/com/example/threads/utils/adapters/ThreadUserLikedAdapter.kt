package com.example.threads.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadUserLikedResponse
import com.example.threads.utils.FeedAdapter

class ThreadUserLikedAdapter(
    private val userList: MutableList<ThreadUserLikedResponse>,
) : RecyclerView.Adapter<ThreadUserLikedAdapter.ThreadViewHolder>() {
    interface OnItemClickListener {
        fun getUserLikedThread(threadId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_view_liked_user, parent, false)
        return ThreadViewHolder(view)
    }

    private var clickListener: FeedAdapter.OnItemClickListener? = null

    fun setOnItemClickListener(listener: FeedAdapter.OnItemClickListener) {
        clickListener = listener
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val userRep = userList[position]
        holder.bind(userRep)

//        holder.itemView.
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.itemUserLikedUsername)
        private val name: TextView = itemView.findViewById(R.id.itemLikedUserName)
        fun bind(user: ThreadUserLikedResponse) {
            username.text = user.username
            name.text = user.name
        }
    }
    fun updateList(newThreads: List<ThreadUserLikedResponse>) {
        userList.clear()
        userList.addAll(newThreads)
        notifyDataSetChanged()
    }
}