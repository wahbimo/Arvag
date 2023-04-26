package com.example.arvag.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.arvag.products_view.ProductCart


@Dao
interface CartDAO {
    @Insert
    fun insertCartItem(productCart: ProductCart?)

    @get:Query("SELECT * FROM product_table")
    val allCartItems: LiveData<List<ProductCart?>?>?

    @Delete
    fun deleteCartItem(productCart: ProductCart?)

    @Query("DELETE FROM product_table")
    fun deleteAllItems()
}