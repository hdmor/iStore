package com.istore.data.repo

import com.istore.data.Banner
import com.istore.data.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(private val bannerDataSource: BannerDataSource) : BannerRepository {

    override fun getBanners(): Single<List<Banner>> = bannerDataSource.getBanners()
}