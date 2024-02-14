package com.istore.data.repo

import com.istore.data.CheckoutResult
import com.istore.data.SubmitOrderResult
import com.istore.data.source.OrderDataSource
import io.reactivex.Single

class OrderRepositoryImpl(private val orderDataSource: OrderDataSource) : OrderRepository {

    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> =
        orderDataSource.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod)

    override fun checkout(orderId: Int): Single<CheckoutResult> = orderDataSource.checkout(orderId)
}