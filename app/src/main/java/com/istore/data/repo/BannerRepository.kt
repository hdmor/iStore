package com.istore.data.repo

import com.istore.data.Banner
import io.reactivex.Single

interface BannerRepository {

    fun getBanners(): Single<List<Banner>>
}