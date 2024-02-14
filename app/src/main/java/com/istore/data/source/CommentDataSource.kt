package com.istore.data.source

import com.istore.data.Comment
import io.reactivex.Single

interface CommentDataSource {

    fun getAll(productId: Int): Single<List<Comment>>
    fun insert(comment: Comment): Single<Comment>
}