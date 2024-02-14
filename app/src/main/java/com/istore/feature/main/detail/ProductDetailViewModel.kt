package com.istore.feature.main.detail

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.common.*
import com.istore.data.AddToCartResponse
import com.istore.data.Comment
import com.istore.data.Product
import com.istore.data.repo.CartRepository
import com.istore.data.repo.CommentRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(
    bundle: Bundle,
    commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) :
    IViewModel() {

    private val _productLiveData = MutableLiveData<Product>()
    val productLiveData: LiveData<Product>
        get() = _productLiveData

    private val _commentsLiveData = MutableLiveData<List<Comment>>()
    val commentsLiveData: LiveData<List<Comment>>
        get() = _commentsLiveData

    init {
        _productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)

        progressBarLiveData.value = true

        commentRepository.getAll(productLiveData.value!!.id).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : ISingleObserver<List<Comment>>(compositeDisposable) {

                override fun onSuccess(t: List<Comment>) {
                    _commentsLiveData.value = t
                }
            })
    }

    fun addToCartBtn(): Completable =
        cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()
}