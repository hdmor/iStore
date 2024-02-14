package com.istore.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.istore.data.Product
import com.istore.data.source.ProductLocalDataSource

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductLocalDataSource

}