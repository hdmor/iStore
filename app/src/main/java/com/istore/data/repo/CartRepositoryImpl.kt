package com.istore.data.repo

import com.istore.data.*
import com.istore.data.source.CartDataSource
import io.reactivex.Single

class CartRepositoryImpl(private val cartDataSource: CartDataSource) : CartRepository {

    override fun addToCart(productId: Int): Single<AddToCartResponse> =
        cartDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> = cartDataSource.get()

    override fun remove(cartItemId: Int): Single<MessageResponse> =
        cartDataSource.remove(cartItemId)

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> =
        cartDataSource.changeCount(cartItemId, count)

    override fun getCartItemsCount(): Single<CartItemsCount> = cartDataSource.getCartItemsCount()
}