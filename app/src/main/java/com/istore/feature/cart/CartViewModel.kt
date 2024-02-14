package com.istore.feature.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.istore.R
import com.istore.common.ISingleObserver
import com.istore.common.IViewModel
import com.istore.common.TAG
import com.istore.common.asyncNetworkRequest
import com.istore.data.*
import com.istore.data.repo.CartRepository
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus

class CartViewModel(private val cartRepository: CartRepository) : IViewModel() {

    private val _cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val cartItemsLiveData: LiveData<List<CartItem>> get() = _cartItemsLiveData

    private val _purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val purchaseDetailLiveData: LiveData<PurchaseDetail> get() = _purchaseDetailLiveData

    private val _emptyStateLiveData = MutableLiveData<EmptyState>()
    val emptyStateLiveData: LiveData<EmptyState> get() = _emptyStateLiveData

    private fun getCartItems() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            progressBarLiveData.value = true
            _emptyStateLiveData.value = EmptyState(false)

            cartRepository.get().asyncNetworkRequest()
                .doFinally { progressBarLiveData.value = false }
                .subscribe(object : ISingleObserver<CartResponse>(compositeDisposable) {

                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            _cartItemsLiveData.value = t.cart_items
                            _purchaseDetailLiveData.value =
                                PurchaseDetail(t.payable_price, t.shipping_cost, t.total_price)
                        } else _emptyStateLiveData.value =
                            EmptyState(true, R.string.add_to_cart_empty_state)
                    }
                })
        } else _emptyStateLiveData.value =
            EmptyState(true, R.string.require_to_login_empty_state, true)
    }

    /**
     * remember doAfterSuccess not run mainThread()
     * so we must use postValue() instead of value for setting new value
     */
    fun removeItemFromCart(cartItem: CartItem): Completable =
        cartRepository.remove(cartItem.cart_item_id)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()

                val cartItemsCount = EventBus.getDefault().getStickyEvent(CartItemsCount::class.java)
                cartItemsCount?.let { c ->
                    c.count -= cartItem.count
                    EventBus.getDefault().postSticky(c)
                }

                cartItemsLiveData.value?.let {
                    if (it.isEmpty()) _emptyStateLiveData.postValue(EmptyState(true, R.string.add_to_cart_empty_state))
                }
            }.ignoreElement()

    fun increaseCartItemCount(cartItem: CartItem): Completable =
        cartRepository.changeCount(cartItem.cart_item_id, ++cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemsCount =
                    EventBus.getDefault().getStickyEvent(CartItemsCount::class.java)
                cartItemsCount?.let { c ->
                    c.count += 1
                    EventBus.getDefault().postSticky(c)
                }
            }.ignoreElement()

    fun decreaseCartItemCount(cartItem: CartItem): Completable =
        cartRepository.changeCount(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemsCount =
                    EventBus.getDefault().getStickyEvent(CartItemsCount::class.java)
                cartItemsCount?.let { c ->
                    c.count -= 1
                    EventBus.getDefault().postSticky(c)
                }
            }.ignoreElement()

    /**
     * view couldn't show getCartItems function
     */
    fun refresh() {
        getCartItems()
    }

    private fun calculateAndPublishPurchaseDetail() {
        cartItemsLiveData.value?.let { cartItems ->
            purchaseDetailLiveData.value?.let { purchaseDetail ->
                var totalPrice = 0
                var payablePrice = 0
                cartItems.forEach {
                    totalPrice += it.product.price * it.count
                    payablePrice += (it.product.price - it.product.discount) * it.count
                }
                purchaseDetail.total_price = totalPrice
                purchaseDetail.payable_price = payablePrice
                _purchaseDetailLiveData.postValue(purchaseDetail)
            }
        }
    }
}