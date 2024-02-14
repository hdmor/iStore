package com.istore.data.source

import com.istore.data.Banner
import com.istore.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {

    override fun getBanners(): Single<List<Banner>> = apiService.getBanners()
}