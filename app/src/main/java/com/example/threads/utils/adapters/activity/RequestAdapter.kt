package com.example.threads.utils.adapters.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.models.ActivityRequest

class RequestAdapter(private val items: List<ActivityRequest>) :
    RecyclerView.Adapter<RequestAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_activity_requests_item, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ActivityRequest) {
            val usernameTextView = itemView.findViewById<TextView>(R.id.txtActivityFollowingUsername)
            val time = itemView.findViewById<TextView>(R.id.txtDateOfFollowed)

            usernameTextView.text = item.username
            time.text= item.time
        }
    }

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            updateItemCount()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            updateItemCount()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            updateItemCount()
        }
    }

    fun setObserver(recyclerView: RecyclerView) {
        registerAdapterDataObserver(observer)
        updateItemCount()
    }

    private fun updateItemCount() {
        itemCountListener?.onItemCountChanged(itemCount)
    }


    interface ItemCountListener {
        fun onItemCountChanged(itemCount: Int)
    }

    private var itemCountListener: ItemCountListener? = null

    fun setItemCountListener(listener: ItemCountListener) {
        itemCountListener = listener
    }
}