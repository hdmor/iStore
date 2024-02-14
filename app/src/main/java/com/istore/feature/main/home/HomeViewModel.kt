package com.istore.feature.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.common.ICompletableObserver
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.data.Banner
import com.istore.data.Product
import com.istore.data.SORT_LATEST
import com.istore.data.SORT_POPULAR
import com.istore.data.repo.BannerRepository
import com.istore.data.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(bannerRepository: BannerRepository, private val productRepository: ProductRepository) :
    IViewModel() {

    private val _bannersLiveData = MutableLiveData<List<Banner>>()
    val bannersLiveData: LiveData<List<Banner>>
        get() = _bannersLiveData

    private val _latestProductsLiveData = MutableLiveData<List<Product>>()
    val latestProductsLiveData: LiveData<List<Product>>
        get() = _latestProductsLiveData

    private val _popularProductsLiveData = MutableLiveData<List<Product>>()
    val popularProductsLiveData: LiveData<List<Product>>
        get() = _popularProductsLiveData

    init {
        progressBarLiveData.value = true

        bannerRepository.getBanners().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ISingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    _bannersLiveData.value = t
                }
            })

        productRepository.getProducts(SORT_LATEST).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ISingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    _latestProductsLiveData.value = t
                }
            })

        productRepository.getProducts(SORT_POPULAR).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : ISingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    _popularProductsLiveData.value = t
                }
            })
    }

    fun addProductToFavorites(product: Product) {
        if (product.isInFavorites)
            productRepository.removeFromFavorites(product).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ICompletableObserver(compositeDisposable) {

                    override fun onComplete() {
                        product.isInFavorites = false
                    }
                })
        else productRepository.addToFavorites(product).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ICompletableObserver(compositeDisposable) {

                override fun onComplete() {
                    product.isInFavorites = true
                }
            })
    }
}