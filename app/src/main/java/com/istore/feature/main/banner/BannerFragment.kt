package com.istore.feature.main.banner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.istore.R
import com.istore.common.EXTRA_KEY_DATA
import com.istore.data.Banner
import com.istore.services.ImageLoadingService
import com.istore.view.IImageView
import org.koin.android.ext.android.inject

class BannerFragment : Fragment() {

    private val imageLoadingService: ImageLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageView = inflater.inflate(R.layout.fragment_banner, container, false) as IImageView
        val banner = requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA)
            ?: throw IllegalStateException("Banner can't be null.")
        imageLoadingService.load(imageView, banner.image)
        return imageView
    }

    companion object {
        fun newInstance(banner: Banner): BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_KEY_DATA, banner)
                }
            }
        }
    }

}