package com.example.threads.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.threads.models.SearchUserInfo

class SearchUserDiffCallback(
    private val oldList: List<SearchUserInfo>,
    private val newList: List<SearchUserInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].id == newList[newPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition] == newList[newPosition]
    }
}