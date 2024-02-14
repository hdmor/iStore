package com.istore.feature.main.banner

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.istore.data.Banner


class BannerSliderAdapter(fragment: Fragment, private val bannersList: List<Banner>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = bannersList.size

    override fun createFragment(position: Int): Fragment =
        BannerFragment.newInstance(bannersList[position])
}