package com.example.threads.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.threads.models.UserRepresentation

class UserFollowersDiffCallback(private val oldList: List<UserRepresentation>, private val newList: List<UserRepresentation>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
