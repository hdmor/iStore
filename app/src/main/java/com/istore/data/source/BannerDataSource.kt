package com.istore.data.source

import com.istore.data.Banner
import io.reactivex.Single

interface BannerDataSource {

    fun getBanners(): Single<List<Banner>>
}