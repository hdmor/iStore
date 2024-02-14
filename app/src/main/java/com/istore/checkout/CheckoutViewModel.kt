package com.istore.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.common.asyncNetworkRequest
import com.istore.data.CheckoutResult
import com.istore.data.repo.OrderRepository

class CheckoutViewModel(orderId: Int, orderRepository: OrderRepository) : IViewModel() {

    private val _checkoutLiveData = MutableLiveData<CheckoutResult>()
    val checkoutLiveData: LiveData<CheckoutResult> get() = _checkoutLiveData

    init {
        orderRepository.checkout(orderId).asyncNetworkRequest()
                .subscribe(object : ISingleObserver<CheckoutResult>(compositeDisposable) {

                    override fun onSuccess(t: CheckoutResult) {
                        _checkoutLiveData.value = t
                    }
                })

    }

}