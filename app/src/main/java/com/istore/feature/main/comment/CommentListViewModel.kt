package com.istore.feature.main.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.common.asyncNetworkRequest
import com.istore.data.Comment
import com.istore.data.repo.CommentRepository

class CommentListViewModel(productId: Int, commentRepository: CommentRepository) : IViewModel() {

    private val _commentsLiveData = MutableLiveData<List<Comment>>()
    val commentsLiveData: LiveData<List<Comment>>
        get() = _commentsLiveData

    init {

        progressBarLiveData.value = true

        commentRepository.getAll(productId).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : ISingleObserver<List<Comment>>(compositeDisposable) {

                override fun onSuccess(t: List<Comment>) {
                    _commentsLiveData.value = t
                }
            })
    }
}