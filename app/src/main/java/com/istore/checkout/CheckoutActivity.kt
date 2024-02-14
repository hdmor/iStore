package com.istore.checkout

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.istore.common.EXTRA_KEY_ID
import com.istore.common.IActivity
import com.istore.common.formatPrice
import com.istore.databinding.ActivityCheckoutBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckoutActivity : IActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    // we can get orderId from 1.ShippingActivity[cash-on-delivery] or 2.Web[online-payment] to pass as parameter for viewModel
    private val viewModel: CheckoutViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null) parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel.checkoutLiveData.observe(this@CheckoutActivity) {
                tvPurchaseStatus.text = if (it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق"
                tvOrderStatus.text = it.payment_status
                tvOrderPrice.text = formatPrice(it.payable_price)
            }
        }
    }
}