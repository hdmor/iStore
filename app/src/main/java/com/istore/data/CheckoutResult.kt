package com.istore.data

data class CheckoutResult(
    val purchase_success: Boolean,
    val payable_price: Int,
    val payment_status: String
)
