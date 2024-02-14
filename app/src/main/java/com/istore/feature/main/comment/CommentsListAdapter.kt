package com.istore.feature.main.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.istore.R
import com.istore.data.Comment

class CommentsListAdapter(private val displayAll: Boolean = false) :
    RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder>() {

    var commentsList = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val commentTitleTv: TextView = itemView.findViewById(R.id.tv_comment_title)
        private val commentDateTv: TextView = itemView.findViewById(R.id.tv_comment_date)
        private val commentAuthorTv: TextView = itemView.findViewById(R.id.tv_comment_author)
        private val commentContentTv: TextView = itemView.findViewById(R.id.tv_comment_content)

        fun bind(comment: Comment) {
            commentTitleTv.text = comment.title
            commentDateTv.text = comment.date
            commentAuthorTv.text = comment.author.email
            commentContentTv.text = comment.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )

    override fun getItemCount(): Int = if (commentsList.size > 3 && !displayAll) 3 else commentsList.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentsList[position])
    }
}