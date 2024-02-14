package com.istore.feature.main.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.istore.R
import com.istore.common.formatPrice
import com.istore.common.implementSpringAnimationTrait
import com.istore.data.Product
import com.istore.services.ImageLoadingService
import com.istore.view.IImageView

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductsListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    private val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductsListAdapter.ProductViewHolder>() {

    var productEventListener: ProductEventListener? = null
    var productsList = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface ProductEventListener {
        fun onProductClick(product: Product)
        fun onFavoriteButtonClick(product: Product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImage: IImageView = itemView.findViewById(R.id.iv_product_image)
        private val productTitleTv: TextView = itemView.findViewById(R.id.tv_product_title)
        private val productPreviousPriceTv: TextView = itemView.findViewById(R.id.tv_product_previous_price)
        private val productCurrentPriceTv: TextView = itemView.findViewById(R.id.tv_product_current_price)
        private val productFavoriteIv: ImageView = itemView.findViewById(R.id.iv_product_favorite)

        fun bindProduct(product: Product) {
            imageLoadingService.load(productImage, product.image)
            productTitleTv.text = product.title
            productPreviousPriceTv.text = formatPrice(product.previous_price)
            productPreviousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            productCurrentPriceTv.text = formatPrice(product.previous_price)
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productEventListener?.onProductClick(product)
            }

            if (product.isInFavorites) productFavoriteIv.setImageResource(R.drawable.ic_favorite_fill)
            else productFavoriteIv.setImageResource(R.drawable.ic_favorites)

            productFavoriteIv.setOnClickListener {
                productEventListener?.onFavoriteButtonClick(product)
                product.isInFavorites = !product.isInFavorites
                notifyItemChanged(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val resource = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            else -> throw IllegalStateException("invalid view type to inflating products list.")
        }
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(resource, parent, false)
        )
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bindProduct(productsList[position])

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

}