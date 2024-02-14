package com.istore.feature.main.list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.istore.R
import com.istore.common.EXTRA_KEY_DATA
import com.istore.common.IActivity
import com.istore.data.Product
import com.istore.databinding.ActivityProductListBinding
import com.istore.feature.main.common.ProductsListAdapter
import com.istore.feature.main.common.VIEW_TYPE_LARGE
import com.istore.feature.main.common.VIEW_TYPE_SMALL
import com.istore.feature.main.detail.ProductDetailActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductListActivity : IActivity(), ProductsListAdapter.ProductEventListener {

    private lateinit var binding: ActivityProductListBinding
    private val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }
    private val adapter: ProductsListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter.productEventListener = this

        val gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)

        binding.apply {
            rvProductsList.layoutManager = gridLayoutManager
            rvProductsList.adapter = adapter

            itbProductList.onBackClickListener = View.OnClickListener { finish() }

            vChangeSort.setOnClickListener {
                MaterialAlertDialogBuilder(this@ProductListActivity).setTitle(R.string.sort)
                    .setSingleChoiceItems(
                        R.array.sorts_list,
                        viewModel.sort
                    ) { dialog, which ->
                        dialog.dismiss()
                        viewModel.onChangedSort(which)
                    }.show()
            }

            ivChangeViewType.setOnClickListener {
                if (adapter.viewType == VIEW_TYPE_SMALL) {
                    ivChangeViewType.setImageResource(R.drawable.ic_view_type_large)
                    adapter.viewType = VIEW_TYPE_LARGE
                    gridLayoutManager.spanCount = 1
                } else {
                    ivChangeViewType.setImageResource(R.drawable.ic_view_type_grid)
                    adapter.viewType = VIEW_TYPE_SMALL
                    gridLayoutManager.spanCount = 2
                }
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        viewModel.selectedSort.observe(this) {
            binding.tvSelectedSort.text = getString(it)
        }
        viewModel.productsLiveData.observe(this) {
            adapter.productsList = it as ArrayList<Product>
        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteButtonClick(product: Product) {
        viewModel.addProductToFavorites(product)
    }
}