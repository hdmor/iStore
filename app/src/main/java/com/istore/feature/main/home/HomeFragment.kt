package com.istore.feature.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istore.common.EXTRA_KEY_DATA
import com.istore.common.IFragment
import com.istore.common.convertDpToPixel
import com.istore.data.Product
import com.istore.data.SORT_LATEST
import com.istore.data.SORT_POPULAR
import com.istore.databinding.FragmentHomeBinding
import com.istore.feature.main.banner.BannerSliderAdapter
import com.istore.feature.main.common.ProductsListAdapter
import com.istore.feature.main.common.VIEW_TYPE_ROUND
import com.istore.feature.main.detail.ProductDetailActivity
import com.istore.feature.main.list.ProductListActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : IFragment(), ProductsListAdapter.ProductEventListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val latestProductsListAdapter: ProductsListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    private val popularProductsListAdapter: ProductsListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        latestProductsListAdapter.productEventListener = this
        popularProductsListAdapter.productEventListener = this

        binding.apply {
            rvLatestProducts.adapter = latestProductsListAdapter
            rvPopularProducts.adapter = popularProductsListAdapter

            btnViewLatestProducts.setOnClickListener { redirectToViewAll(SORT_LATEST) }
            btnViewPopularProducts.setOnClickListener { redirectToViewAll(SORT_POPULAR) }
        }


        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            val vp2BannerSlider = binding.vp2BannerSlider
            vp2BannerSlider.adapter = bannerSliderAdapter
            val vp2LayoutParams = vp2BannerSlider.layoutParams
            vp2LayoutParams.height =
                (((vp2BannerSlider.measuredWidth - convertDpToPixel(
                    32f,
                    requireContext()
                )) * 173) / 328).toInt()
            vp2BannerSlider.layoutParams = vp2LayoutParams
            binding.diBannerIndicator.attachTo(vp2BannerSlider)
        }

        homeViewModel.latestProductsLiveData.observe(viewLifecycleOwner) {
            latestProductsListAdapter.productsList = it as ArrayList<Product>
        }

        homeViewModel.popularProductsLiveData.observe(viewLifecycleOwner) {
            popularProductsListAdapter.productsList = it as ArrayList<Product>
        }
    }

    private fun redirectToViewAll(sort: Int) {
        startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, sort)
        })
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteButtonClick(product: Product) {
        homeViewModel.addProductToFavorites(product)
    }

}