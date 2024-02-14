package com.istore.services

import com.istore.view.IImageView

interface ImageLoadingService {
    fun load(imageView: IImageView, imageUrl: String)
}