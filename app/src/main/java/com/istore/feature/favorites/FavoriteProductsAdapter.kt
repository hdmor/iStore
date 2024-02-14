package com.istore.feature.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.istore.data.Product
import com.istore.databinding.ItemFavoriteProductBinding
import com.istore.services.ImageLoadingService

class FavoriteProductsAdapter(
    private val favoriteProducts: MutableList<Product>,
    private val favoriteProductEventListener: FavoriteProductEventListener,
    private val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<FavoriteProductsAdapter.FavoriteProductViewHolder>() {

    interface FavoriteProductEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product)
    }

    inner class FavoriteProductViewHolder(private val binding: ItemFavoriteProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindProduct(product: Product) {
            binding.tvFavoriteProductTitle.text = product.title
            imageLoadingService.load(binding.ivFavoriteProductImage, product.image)

            binding.root.setOnClickListener { favoriteProductEventListener.onClick(product) }
            binding.root.setOnLongClickListener {
                favoriteProducts.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductEventListener.onLongClick(product)
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder =
        FavoriteProductViewHolder(ItemFavoriteProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = favoriteProducts.size

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        holder.bindProduct(favoriteProducts[position])
    }
}