package com.istore.data.repo

import com.istore.data.Comment
import com.istore.data.source.CommentDataSource
import io.reactivex.Single

class CommentRepositoryImpl(private val commentDataSource: CommentDataSource) : CommentRepository {

    override fun getAll(productId: Int): Single<List<Comment>> = commentDataSource.getAll(productId)

    override fun insert(comment: Comment): Single<Comment> {
        TODO("Not yet implemented")
    }
}