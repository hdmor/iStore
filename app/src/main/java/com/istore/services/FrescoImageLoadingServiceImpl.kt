package com.istore.services

import com.facebook.drawee.view.SimpleDraweeView
import com.istore.view.IImageView

class FrescoImageLoadingServiceImpl : ImageLoadingService {

    override fun load(imageView: IImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
    }
}