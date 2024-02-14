package com.istore.data.source

import com.istore.data.CheckoutResult
import com.istore.data.SubmitOrderResult
import io.reactivex.Single

interface OrderDataSource {

    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult>

    fun checkout(orderId: Int): Single<CheckoutResult>
}