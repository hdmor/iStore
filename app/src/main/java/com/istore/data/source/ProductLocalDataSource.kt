package com.istore.data.source

import androidx.room.*
import com.istore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

/**
 * this class play room database DAO rule
 */
@Dao
interface ProductLocalDataSource : ProductDataSource {

    override fun getProducts(sort: Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM favorite_products")
    override fun getFavoriteProducts(): Single<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun removeFromFavorites(product: Product): Completable
}