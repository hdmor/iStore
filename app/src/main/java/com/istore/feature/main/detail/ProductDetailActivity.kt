package com.istore.feature.main.detail

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.istore.R
import com.istore.common.EXTRA_KEY_ID
import com.istore.common.IActivity
import com.istore.common.ICompletableObserver
import com.istore.common.formatPrice
import com.istore.data.Comment
import com.istore.databinding.ActivityProductDetailBinding
import com.istore.feature.main.comment.CommentListActivity
import com.istore.feature.main.comment.CommentsListAdapter
import com.istore.services.ImageLoadingService
import com.istore.view.scroll.ObservableScrollViewCallbacks
import com.istore.view.scroll.ScrollState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailActivity : IActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private val commentsListAdapter = CommentsListAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            rvProductDetailsCommentsList.adapter = commentsListAdapter

            ivProductDetailsImage.post {

                val productIvHeight = binding.ivProductDetailsImage.height
                val productDetailToolBar = binding.cvProductDetailsToolbar
                val productImageView = binding.ivProductDetailsImage
                binding.observableScrollView.addScrollViewCallbacks(object :
                    ObservableScrollViewCallbacks {

                    override fun onScrollChanged(
                        scrollY: Int,
                        firstScroll: Boolean,
                        dragging: Boolean
                    ) {
                        productDetailToolBar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                        productImageView.translationY = scrollY.toFloat() / 2
                    }

                    override fun onDownMotionEvent() {}
                    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {}
                })
            }

            efabAddToCart.setOnClickListener {
                viewModel.addToCartBtn().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : ICompletableObserver(compositeDisposable) {

                        override fun onComplete() {
                            showSnackBar(getString(R.string.add_to_cart_success_message))
                        }
                    })
            }
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        viewModel.productLiveData.observe(this) {
            imageLoadingService.load(binding.ivProductDetailsImage, it.image)
            binding.tvProductDetailsTitle.text = it.title
            binding.tvProductDetailsPreviousPrice.text = formatPrice(it.previous_price)
            binding.tvProductDetailsPreviousPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvProductDetailsCurrentPrice.text = formatPrice(it.price)
            binding.tvProductDetailsToolbarTitle.text = it.title
        }

        viewModel.commentsLiveData.observe(this) {
            commentsListAdapter.commentsList = it as ArrayList<Comment>
            if (it.size > 3) {
                binding.btnViewAllComments.visibility = View.VISIBLE
                binding.btnViewAllComments.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID, viewModel.productLiveData.value!!.id)
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}