package com.example.arvag.listener

import com.example.arvag.products_view.Product

/**
 * Interface definition for a callback to be invoked when product data is loaded successfully or failed to load.
 */
interface IProductLoadListener {

    /**
     * Called when product data is loaded successfully.
     *
     * @param productModelList The list of products that were loaded
     */
    fun onProductLoadSuccess(productModelList: List<Product>?)

    /**
     * Called when product data fails to load.
     *
     * @param message The error message indicating the reason for the failure
     */
    fun onProductLoadFailed(message:String?)
}