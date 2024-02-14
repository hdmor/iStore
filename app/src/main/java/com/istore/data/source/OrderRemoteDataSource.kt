package com.istore.data.source

import com.google.gson.JsonObject
import com.istore.data.CheckoutResult
import com.istore.data.SubmitOrderResult
import com.istore.services.http.ApiService
import io.reactivex.Single

class OrderRemoteDataSource(private val apiService: ApiService) : OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> = apiService.submitOrder(JsonObject().apply {
        addProperty("first_name", firstName)
        addProperty("last_name", lastName)
        addProperty("postal_code", postalCode)
        addProperty("mobile", phoneNumber)
        addProperty("address", address)
        addProperty("payment_method", paymentMethod)
    })

    override fun checkout(orderId: Int): Single<CheckoutResult> = apiService.checkout(orderId)
}