package com.example.arvag.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.arvag.dao.CartDAO
import com.example.arvag.products_view.Product


@Database(entities = [Product::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDAO(): CartDAO?

    companion object {
        private var instance: CartDatabase? = null
        @Synchronized
        fun getInstance(context: Context): CartDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext, CartDatabase::class.java, "ProductDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}