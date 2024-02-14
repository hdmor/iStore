package com.istore.data

/**
 * we need to save state of progressBar visibility in variable for each cartItem
 */
data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var loadingVisibility: Boolean = false
)