package com.example.arvag.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.arvag.products_view.Product


@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM product_table ORDER BY id DESC")
    suspend fun getAllProducts():List<Product>

    @Delete
    suspend fun deleteProductItem(product: Product)

    @Query("DELETE FROM product_table")
    suspend fun deleteAllItems()

    @Query("SELECT * FROM product_table WHERE id = :id")
    suspend fun getById(id: Int): Product
}