package com.example.arvag.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.arvag.dao.ProductDao
import com.example.arvag.products_view.Product


@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        //private const val DB_NAME = "ProductDatabase"

        @Volatile
        private var instance: ProductDataBase? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDataBase(context).also {
                instance = it
            }
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ProductDataBase::class.java,
            "product-database"
        ).build()
    }
}

