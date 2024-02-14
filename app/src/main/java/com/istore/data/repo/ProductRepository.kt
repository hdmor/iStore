package com.istore.data.repo

import com.istore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {

    // from remote data source
    fun getProducts(sort: Int): Single<List<Product>>

    // from local data source
    fun getFavoriteProducts(): Single<List<Product>>
    fun addToFavorites(product: Product): Completable
    fun removeFromFavorites(product: Product): Completable
}