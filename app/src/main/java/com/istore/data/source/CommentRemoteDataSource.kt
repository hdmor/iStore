package com.istore.data.source

import com.istore.data.Comment
import com.istore.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(private val apiService: ApiService) : CommentDataSource {

    override fun getAll(productId: Int): Single<List<Comment>> = apiService.getComments(productId)

    override fun insert(comment: Comment): Single<Comment> {
        TODO("Not yet implemented")
    }
}