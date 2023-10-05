package com.example.threads.utils.adapters.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threads.R
import com.example.threads.data.models.CommentResponse
import com.example.threads.data.models.ThreadResponse
import com.example.threads.data.models.ThreadWithCommentsResponse
import kotlinx.android.synthetic.main.custom_comment_under_thread.view.checkBoxComment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


//class ThreadCommentAdapter(private var comments: List<CommentResponse>) :
//    RecyclerView.Adapter<ThreadCommentAdapter.CommentViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.custom_comment_under_thread, parent, false)
//        return CommentViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
//        val comment = comments[position]
//        holder.bind(comment)
//    }
//
//    override fun getItemCount(): Int = comments.size
//
//    fun updateComments(newComments: List<CommentResponse>) {
//        comments = newComments
//        notifyDataSetChanged()
//    }
//
//    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(comment: CommentResponse) {
//            val usernameTextView = itemView.findViewById<TextView>(R.id.commentThreadUsername)
//            val commentTextView = itemView.findViewById<TextView>(R.id.commentThread)
//            val timeMinutesTextView = itemView.findViewById<TextView>(R.id.commentThreadTime)
//            val formattedDate = formatDate(comment.created)
//
//            timeMinutesTextView.text = formattedDate
//            usernameTextView.text = comment.user
//            commentTextView.text = comment.content
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
//}

class ThreadCommentAdapter(
    private val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<ThreadCommentAdapter.CommentViewHolder>() {

    private var currentList: List<CommentResponse> = emptyList()

    interface OnItemClickListener {
        fun likeComment(commentId: Int)
    }

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_comment_under_thread, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = currentList[position]
        holder.bind(comment)

        holder.itemView.checkBoxComment.setOnClickListener {
            onItemClickListener?.likeComment(comment.id)
        }
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
            val usernameTextView = itemView.findViewById<TextView>(R.id.commentThreadUsername)
            val commentTextView = itemView.findViewById<TextView>(R.id.commentThread)
            val timeMinutesTextView = itemView.findViewById<TextView>(R.id.commentThreadTime)
            val commentLike = itemView.findViewById<TextView>(R.id.commentsCounterLikes)
            val formattedDate = formatDate(comment.created)

            timeMinutesTextView.text = formattedDate
            usernameTextView.text = comment.user
            commentTextView.text = comment.content

            val numLikes = comment.likes
            commentLike.text = if (numLikes == 1) {
                "$numLikes like"
            } else {
                "$numLikes likes"
            }
        }
    }

    fun formatDate(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val parsedDate = dateFormat.parse(dateString)

        parsedDate?.let {
            val timeZoneOffsetMillis = 6 * 60 * 60 * 1000
            val adjustedDate = Date(it.time + timeZoneOffsetMillis)

            val now = Calendar.getInstance().time
            val diffInMillis = now.time - adjustedDate.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            val years = days / 365L

            return when {
                years > 0 -> "$years${if (years == 1L) "y" else "y"}"
                days > 0 -> "$days${if (days == 1L) "d" else "d"}"
                hours > 0 -> "$hours${if (hours == 1L) "h" else "h"}"
                minutes > 0 -> "$minutes${if (minutes == 1L) "m" else "m"}"
                else -> "<1"
            }
        }
        return ""
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

