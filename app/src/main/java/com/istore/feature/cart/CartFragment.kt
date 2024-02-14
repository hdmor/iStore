package com.istore.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.istore.R
import com.istore.common.EXTRA_KEY_DATA
import com.istore.common.ICompletableObserver
import com.istore.common.IFragment
import com.istore.data.CartItem
import com.istore.databinding.FragmentCartBinding
import com.istore.feature.auth.AuthActivity
import com.istore.feature.main.detail.ProductDetailActivity
import com.istore.services.ImageLoadingService
import com.istore.shipping.ShippingActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : IFragment(), CartItemAdapter.CartItemViewCallbacks {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModel()
    private var adapter: CartItemAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.efabPay.setOnClickListener {
            startActivity(Intent(requireContext(), ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, viewModel.purchaseDetailLiveData.value)
            })
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            adapter = CartItemAdapter(it as MutableList<CartItem>, imageLoadingService, this)
            binding.rvCartItemsList.adapter = adapter
        }

        viewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) {
            adapter?.let { adapter ->
                adapter.purchaseDetail = it
                adapter.notifyItemChanged(adapter.cartItemsList.size)
            }
        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            if (it.visibility) {
                val emptyState = showEmptyState(R.layout.view_cart_empty_state)
                if (emptyState != null) {
                    view.findViewById<TextView>(R.id.tv_empty_state_message).text =
                        getString(it.messageResourceId)
                    view.findViewById<MaterialButton>(R.id.mbtn_empty_state_call_to_action).visibility =
                        if (it.callToActionButtonVisibility) View.VISIBLE else View.GONE
                    view.findViewById<MaterialButton>(R.id.mbtn_empty_state_call_to_action)
                        .setOnClickListener {
                            startActivity(Intent(requireContext(), AuthActivity::class.java))
                        }
                } else
                    view.findViewById<FrameLayout>(R.id.fl_empty_state_root_view).visibility =
                        View.GONE
            }
        }
    }

    /**
     * when start fragment every time
     * you need to catch fresh list of cartItems from server by calling refresh function
     */
    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onRemoveCartItemClick(cartItem: CartItem) {
        viewModel.removeItemFromCart(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ICompletableObserver(compositeDisposable) {

                override fun onComplete() {
                    adapter?.removeCartItem(cartItem)
                }
            })
    }

    override fun onIncreaseCartItemClick(cartItem: CartItem) {
        viewModel.increaseCartItemCount(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ICompletableObserver(compositeDisposable) {

                override fun onComplete() {
                    adapter?.changeCartItemsCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemClick(cartItem: CartItem) {
        viewModel.decreaseCartItemCount(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ICompletableObserver(compositeDisposable) {

                override fun onComplete() {
                    adapter?.changeCartItemsCount(cartItem)
                }
            })
    }

    override fun onCartItemImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }
}