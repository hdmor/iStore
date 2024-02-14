package com.istore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartResponse(
    val cart_items: List<CartItem>,
    val payable_price: Int,
    val shipping_cost: Int,
    val total_price: Int
)


/**
 * we're separate cartResponse to two dataClass
 * because in recyclerView we have two viewType :
 * 1. for showing items as cart list
 * 2. showing purchase detail
 */
@Parcelize
data class PurchaseDetail(
    var payable_price: Int,
    var shipping_cost: Int,
    var total_price: Int
) : Parcelable