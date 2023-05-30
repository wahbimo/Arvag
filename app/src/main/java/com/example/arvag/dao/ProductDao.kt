package com.example.arvag.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.arvag.products_view.Product

/**
 * Data Access Object (DAO) for the Product entity.
 */
@Dao
interface ProductDao {
    /**
     * Inserts a product into the database.
     *
     * @param product The product to insert.
     */
    @Insert
    suspend fun insertProduct(product: Product)

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    @Query("SELECT * FROM product_table ORDER BY id DESC")
    suspend fun getAllProducts():List<Product>

    /**
     * Deletes a product from the database.
     *
     * @param product The product to delete.
     */
    @Delete
    suspend fun deleteProductItem(product: Product)

    /**
     * Deletes all products from the database.
     */
    @Query("DELETE FROM product_table")
    suspend fun deleteAllItems()

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id The ID of the product.
     * @return The product with the specified ID.
     */
    @Query("SELECT * FROM product_table WHERE id = :id")
    suspend fun getById(id: Int): Product
}