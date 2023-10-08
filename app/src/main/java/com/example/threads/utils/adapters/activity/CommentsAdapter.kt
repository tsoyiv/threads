package com.example.threads.utils.adapters.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import com.example.threads.models.ActivityComments
import com.example.threads.view.likes_fragments.CommentsFragmentDirections
import com.example.threads.view.main_feed_fragments.TabMainFeedFragmentDirections
import com.example.threads.view.user_fragments.UserMainFragmentDirections
import kotlinx.android.synthetic.main.custom_comment_under_thread.view.checkBoxComment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

//class CommentsAdapter(private val items: MutableList<CommentResponse>) :
//    RecyclerView.Adapter<CommentsAdapter.FollowerViewHolder>() {
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): FollowerViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.custom_activity_comments, parent, false)
//        return FollowerViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
//
////        holder.itemView.setOnClickListener {
////            val action =
////                CommentsFragmentDirections.actionCommentsFragmentToThreadDescFragment2(item)
////            action.thread = item
////            holder.itemView.findNavController().navigate(action)
////        }
//    }
//
//    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(item: CommentResponse) {
//
//            val commentUsername = itemView.findViewById<TextView>(R.id.item_user_username123)
//            val comment = itemView.findViewById<TextView>(R.id.txtComment)
//            val thread_content = itemView.findViewById<TextView>(R.id.txtThread)
//
//            commentUsername.text = item.user
//            comment.text = item.content
//            thread_content.text = item.thread_content
//

////
////            val commentUsername = itemView.findViewById<TextView>(R.id.item_user_username123)
////            val comment = itemView.findViewById<TextView>(R.id.txtComment)
////            val timeCommented = itemView.findViewById<TextView>(R.id.txtTimeCommented)
////
////            item.comments.forEach {
////                commentUsername.text = it.user
////
////                //comment.text = it.content
////

////
////                val formattedDate = formatDate(it.created)
////                timeCommented.text = formattedDate
////            }
//        }
//    }
//
//    fun formatDate(dateString: String): String {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val parsedDate = dateFormat.parse(dateString)
//
//        parsedDate?.let {
//            val timeZoneOffsetMillis = 6 * 60 * 60 * 1000
//            val adjustedDate = Date(it.time + timeZoneOffsetMillis)
//
//            val now = Calendar.getInstance().time
//            val diffInMillis = now.time - adjustedDate.time
//            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
//            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
//            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
//            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
//            val years = days / 365L
//
//            return when {
//                years > 0 -> "$years${if (years == 1L) "y" else "y"}"
//                days > 0 -> "$days${if (days == 1L) "d" else "d"}"
//                hours > 0 -> "$hours${if (hours == 1L) "h" else "h"}"
//                minutes > 0 -> "$minutes${if (minutes == 1L) "m" else "m"}"
//                else -> "<1"
//            }
//        }
//        return ""
//    }
//
//    fun updateList(newThreads: List<CommentResponse>) {
//        items.clear()
//        items.addAll(newThreads)
//        notifyDataSetChanged()
//    }
//}

class CommentsAdapter(
    private val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var currentList: List<CommentResponse> = emptyList()

    interface OnItemClickListener {
        //fun likeComment(commentId: Int)
    }

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_activity_comments, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = currentList[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int = currentList.size

    fun updateComments(newComments: List<CommentResponse>) {
        val diffCallback = CommentDiffCallback(currentList, newComments)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        currentList = newComments
        diffResult.dispatchUpdatesTo(this)
    }


    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: CommentResponse) {

            val thread = itemView.findViewById<TextView>(R.id.txtThread)
            val comment1 = itemView.findViewById<TextView>(R.id.txtComment)
            val username = itemView.findViewById<TextView>(R.id.item_user_username123)
            username.text = comment.user

            val fullComment = comment.content
            val maxWordsToShow = 5

            val words = fullComment.split(" ")
            val truncatedComment = if (words.size > maxWordsToShow) {
                words.take(maxWordsToShow).joinToString(" ") + "..."
            } else {
                fullComment
            }
            comment1.text = truncatedComment

            val fullThread = comment.thread_content
            val truncatedThread = if (words.size > maxWordsToShow) {
                words.take(maxWordsToShow).joinToString(" ") + "..."
            } else {
                fullThread
            }
            thread.text = truncatedThread
        }
    }

    private class CommentDiffCallback(
        private val oldList: List<CommentResponse>,
        private val newList: List<CommentResponse>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
