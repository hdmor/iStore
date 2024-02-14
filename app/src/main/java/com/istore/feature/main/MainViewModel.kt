package com.istore.feature.main

import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.data.CartItemsCount
import com.istore.data.TokenContainer
import com.istore.data.repo.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * this viewModel created for MainActivity for update cartBadge items
 * we are not need to observeOn(AndroidSchedulers.mainThread())
 * we need to use eventBus sticky event
 */
class MainViewModel(private val cartRepository: CartRepository) : IViewModel() {

    fun getCartItemsCount() {
        if (!TokenContainer.token.isNullOrEmpty())
            cartRepository.getCartItemsCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object : ISingleObserver<CartItemsCount>(compositeDisposable) {

                    override fun onSuccess(t: CartItemsCount) {
                        EventBus.getDefault().postSticky(t)
                    }
                })
    }

}