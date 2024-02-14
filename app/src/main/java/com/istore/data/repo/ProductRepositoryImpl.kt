package com.istore.data.repo

import com.istore.data.Product
import com.istore.data.source.ProductDataSource
import com.istore.data.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(private val productRemoteDataSource: ProductDataSource, private val productLocalDataSource: ProductDataSource) :
    ProductRepository {

    /**
     * check localFavoriteProducts id with received products from server id
     * to detecting favorites products and enable isInFavorites field
     */
    override fun getProducts(sort: Int): Single<List<Product>> = productLocalDataSource.getFavoriteProducts().flatMap { favoriteProducts ->
        productRemoteDataSource.getProducts(sort).doOnSuccess { onlineProducts ->
            val favoriteProductsIds = favoriteProducts.map { it.id }
            onlineProducts.forEach {product ->
                if (favoriteProductsIds.contains(product.id)) product.isInFavorites = true
            }
        }
    }

    override fun getFavoriteProducts(): Single<List<Product>> = productLocalDataSource.getFavoriteProducts()

    override fun addToFavorites(product: Product): Completable = productLocalDataSource.addToFavorites(product)

    override fun removeFromFavorites(product: Product): Completable = productLocalDataSource.removeFromFavorites(product)
}