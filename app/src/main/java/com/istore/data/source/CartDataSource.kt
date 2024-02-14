package com.istore.data.source

import com.istore.data.*
import io.reactivex.Single

interface CartDataSource {

    fun addToCart(productId: Int): Single<AddToCartResponse>
    fun get(): Single<CartResponse>
    fun remove(cartItemId: Int): Single<MessageResponse>
    fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse>
    fun getCartItemsCount(): Single<CartItemsCount>
}