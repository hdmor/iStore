package com.istore.data.repo

import com.istore.data.Comment
import io.reactivex.Single

interface CommentRepository {

    fun getAll(productId: Int): Single<List<Comment>>
    fun insert(comment: Comment): Single<Comment>
}