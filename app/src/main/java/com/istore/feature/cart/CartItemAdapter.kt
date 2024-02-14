package com.istore.feature.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.istore.common.formatPrice
import com.istore.data.CartItem
import com.istore.data.PurchaseDetail
import com.istore.databinding.ItemCartBinding
import com.istore.databinding.ItemPurchaseDetailBinding
import com.istore.services.ImageLoadingService

/**
 * we use RecyclerView.ViewHolder for generic type to adapter
 * because we are two different type of viewHolder in this class
 * 1. for display cartItems 2. for display purchase detail
 */

const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAIL = 1

class CartItemAdapter(
    val cartItemsList: MutableList<CartItem>,
    private val imageLoadingService: ImageLoadingService,
    private val cartItemViewCallbacks: CartItemViewCallbacks
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var purchaseDetail: PurchaseDetail? = null

    interface CartItemViewCallbacks {
        fun onRemoveCartItemClick(cartItem: CartItem)
        fun onIncreaseCartItemClick(cartItem: CartItem)
        fun onDecreaseCartItemClick(cartItem: CartItem)
        fun onCartItemImageClick(cartItem: CartItem)
    }

    inner class CartItemViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCartItem(cartItem: CartItem) {
            imageLoadingService.load(binding.ivCartItemImage, cartItem.product.image)
            binding.tvCartItemTitle.text = cartItem.product.title
            binding.tvCartItemCount.text = cartItem.count.toString()
            binding.tvCartItemPreviousPrice.text =
                formatPrice(cartItem.product.price + cartItem.product.discount)
            binding.tvCartItemCurrentPrice.text = formatPrice(cartItem.product.price)
            binding.pbCartItemChangeCountLoading.visibility =
                if (cartItem.loadingVisibility) View.VISIBLE else View.GONE
            binding.tvCartItemCount.visibility =
                if (cartItem.loadingVisibility) View.INVISIBLE else View.VISIBLE


            binding.ivCartItemImage.setOnClickListener {
                cartItemViewCallbacks.onCartItemImageClick(cartItem)
            }

            binding.ivItemCartIncrease.setOnClickListener {
                if (cartItem.count < 5) {
                    cartItem.loadingVisibility = true
                    binding.pbCartItemChangeCountLoading.visibility = View.VISIBLE
                    binding.tvCartItemCount.visibility = View.INVISIBLE
                    cartItemViewCallbacks.onIncreaseCartItemClick(cartItem)
                }
            }
            binding.ivItemCartDecrease.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.loadingVisibility = true
                    binding.pbCartItemChangeCountLoading.visibility = View.VISIBLE
                    binding.tvCartItemCount.visibility = View.INVISIBLE
                    cartItemViewCallbacks.onDecreaseCartItemClick(cartItem)
                }
            }
            binding.tvCartItemRemoveFromCart.setOnClickListener {
                cartItemViewCallbacks.onRemoveCartItemClick(cartItem)
            }
        }
    }

    /**
     * this class isn't inner class because we need this class in ShippingActivity
     * and we must access by static class
     */
    class PurchaseDetailViewHolder(private val binding: ItemPurchaseDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {
            binding.tvPurchaseDetailTotalPrice.text = formatPrice(totalPrice)
            binding.tvPurchaseDetailShippingCost.text = formatPrice(shippingCost)
            binding.tvPurchaseDetailPayablePrice.text = formatPrice(payablePrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART_ITEM)
            CartItemViewHolder(
                ItemCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else PurchaseDetailViewHolder(
            ItemPurchaseDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // +1 for showing purchase detail cart
    override fun getItemCount(): Int = cartItemsList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder) holder.bindCartItem(cartItemsList[position])
        else if (holder is PurchaseDetailViewHolder) purchaseDetail?.let {
            holder.bind(it.total_price, it.shipping_cost, it.payable_price)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItemsList.size) VIEW_TYPE_PURCHASE_DETAIL else VIEW_TYPE_CART_ITEM
    }

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItemsList.indexOf(cartItem)
        if (index > -1) {
            cartItemsList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun changeCartItemsCount(cartItem: CartItem) {
        val index = cartItemsList.indexOf(cartItem)
        if (index > -1) {
            cartItemsList[index].loadingVisibility = false
            notifyItemChanged(index)
        }
    }
}