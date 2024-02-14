package com.istore.shipping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.istore.R
import com.istore.checkout.CheckoutActivity
import com.istore.common.*
import com.istore.data.PurchaseDetail
import com.istore.data.SubmitOrderResult
import com.istore.databinding.ActivityShippingBinding
import com.istore.feature.cart.CartItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * we can use only disposable for this activity [because for we have just one request]
 * just for standing with project structure we use compositeDisposable
 */
class ShippingActivity : IActivity() {

    private lateinit var binding: ActivityShippingBinding
    private val viewModel: ShippingViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val purchaseDetail = intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
                ?: throw IllegalStateException("you must have Purchase Detail")

        val purchaseDetailViewHolder =
                CartItemAdapter.PurchaseDetailViewHolder(binding.iPurchaseDetailIncludeView)
        purchaseDetailViewHolder.bind(
                purchaseDetail.total_price,
                purchaseDetail.shipping_cost,
                purchaseDetail.payable_price
        )

        binding.apply {

            val onClickListener = View.OnClickListener {
                viewModel.submitOrder(
                        tietShippingFirstName.text.toString(),
                        tietShippingLastName.text.toString(),
                        tietShippingPostalCode.text.toString(),
                        tietShippingPhoneNumber.text.toString(),
                        tietShippingRecipientAddress.text.toString(),
                        if (it.id == R.id.mbtn_online_payment) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : ISingleObserver<SubmitOrderResult>(compositeDisposable) {

                            override fun onSuccess(t: SubmitOrderResult) {

                                if (t.bank_gateway_url.isNotEmpty())
                                    openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url) // we must redirect to browser
                                else startActivity(Intent(this@ShippingActivity, CheckoutActivity::class.java).apply {
                                    putExtra(EXTRA_KEY_ID, t.order_id)
                                })

                                finish() // after order you must be close the shipping page
                            }
                        })
            }

            mbtnOnlinePayment.setOnClickListener(onClickListener)
            mbtnCashOnDelivery.setOnClickListener(onClickListener)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}