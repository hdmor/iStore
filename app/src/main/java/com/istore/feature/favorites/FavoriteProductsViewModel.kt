package com.istore.feature.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.common.ICompletableObserver
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.common.asyncNetworkRequest
import com.istore.data.Product
import com.istore.data.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteProductsViewModel(private val productRepository: ProductRepository) : IViewModel() {

    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> get() = _favoriteProducts

    init {
        progressBarLiveData.value = true
        productRepository.getFavoriteProducts().asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : ISingleObserver<List<Product>>(compositeDisposable) {

                override fun onSuccess(t: List<Product>) {
                    _favoriteProducts.value = t
                }
            })
    }

    fun removeFromFavorites(product: Product) {
        productRepository.removeFromFavorites(product).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ICompletableObserver(compositeDisposable) {

                override fun onComplete() {

                }
            })
    }
}