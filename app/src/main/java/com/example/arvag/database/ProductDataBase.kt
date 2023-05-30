package com.example.arvag.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.arvag.dao.ProductDao
import com.example.arvag.products_view.Product

/**
 * Room database class for managing the Product entity.
 */
@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase : RoomDatabase() {
    /**
     * Provides access to the ProductDao for performing data access operations.
     *
     * @return The ProductDao instance.
     */
    abstract fun productDao(): ProductDao

    companion object {
        //private const val DB_NAME = "ProductDatabase"

        @Volatile
        private var instance: ProductDataBase? = null
        private val LOCK = Any()

        /**
         * Creates or retrieves an instance of the ProductDataBase.
         *
         * @param context The application context.
         * @return The ProductDataBase instance.
         */
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

