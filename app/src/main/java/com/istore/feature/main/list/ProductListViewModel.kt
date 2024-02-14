package com.istore.feature.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.R
import com.istore.common.ICompletableObserver
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.common.asyncNetworkRequest
import com.istore.data.Product
import com.istore.data.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(
    var sort: Int,
    private val productRepository: ProductRepository
) : IViewModel() {

    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>>
        get() = _productsLiveData

    private val _selectedSort = MutableLiveData<Int>()
    val selectedSort: LiveData<Int>
        get() = _selectedSort
    private val sortsList = arrayOf(
        R.string.sort_latest,
        R.string.sort_popular,
        R.string.sort_expensive,
        R.string.sort_cheep
    )

    init {
        getProducts()
        _selectedSort.value = sortsList[sort]
    }

    private fun getProducts() {
        progressBarLiveData.value = true
        productRepository.getProducts(sort).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : ISingleObserver<List<Product>>(compositeDisposable) {

                override fun onSuccess(t: List<Product>) {
                    _productsLiveData.value = t
                }
            })
    }

    fun onChangedSort(sort:Int) {
        this.sort = sort
        _selectedSort.value = sortsList[sort]
        getProducts()
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