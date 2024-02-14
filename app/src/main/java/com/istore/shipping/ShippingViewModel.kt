package com.istore.shipping

import com.istore.common.IViewModel
import com.istore.data.SubmitOrderResult
import com.istore.data.repo.OrderRepository
import io.reactivex.Single

const val PAYMENT_METHOD_COD = "cash_on_delivery"
const val PAYMENT_METHOD_ONLINE = "online"

class ShippingViewModel(private val orderRepository: OrderRepository) : IViewModel() {

    /**
     * we set return type to this function
     * because we need response data of this request
     * for display in view. (without any liveData)
     */
    fun submitOrder(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> =
        orderRepository.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod)

}