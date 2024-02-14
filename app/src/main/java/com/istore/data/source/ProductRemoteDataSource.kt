package com.istore.data.source

import com.istore.data.Product
import com.istore.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(private val apiService: ApiService) : ProductDataSource {

    override fun getProducts(sort: Int): Single<List<Product>> = apiService.getProducts(sort.toString())

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun removeFromFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }
}