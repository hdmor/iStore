package com.istore.feature.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.istore.R
import com.istore.common.EXTRA_KEY_DATA
import com.istore.common.IActivity
import com.istore.data.Product
import com.istore.databinding.ActivityFavoriteProductsBinding
import com.istore.feature.main.detail.ProductDetailActivity
import com.istore.services.ImageLoadingService
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteProductsActivity : IActivity(), FavoriteProductsAdapter.FavoriteProductEventListener {

    private lateinit var binding: ActivityFavoriteProductsBinding
    private val viewModel: FavoriteProductsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            ivInfo.setOnClickListener { showSnackBar(getString(R.string.favorites_info_message)) }

            viewModel.favoriteProducts.observe(this@FavoriteProductsActivity) {
                if (it.isNotEmpty()) {
                    // you can user get() koin replace with inject() class and save it in instance variable
                    val adapter = FavoriteProductsAdapter(it as MutableList<Product>, this@FavoriteProductsActivity, get())
                    rvFavoriteProducts.adapter = adapter
                } else {
                    showEmptyState(R.layout.view_default_empty_state)
                    findViewById<TextView>(R.id.tv_favorite_products_empty_state_message).text = getString(R.string.favorites_empty_state_message)
                }
            }

        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorites(product)
    }
}