package com.istore.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_products")
@Parcelize
data class Product(
    val discount: Int,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val image: String,
    val previous_price: Int,
    val price: Int,
    val status: Int,
    val title: String
) : Parcelable {
    // for saving favorite state we need to have a new variable
    var isInFavorites: Boolean = false
}

const val SORT_LATEST = 0
const val SORT_POPULAR = 1
const val SORT_PRICE_DESC = 2
const val SORT_PRICE_ASC = 3