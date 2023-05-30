package com.example.arvag.listener
/**
 * Interface definition for a callback to be invoked when category data is loaded successfully or failed to load.
 */
interface ICategoryLoadListener {
    /**
     * Called when category data is loaded successfully.
     *
     * @param categoryList The list of categories that were loaded
     */
    fun onCategoryLoadSuccess(categoryList: List<String>?)

    /**
     * Called when category data fails to load.
     *
     * @param message The error message indicating the reason for the failure
     */
    fun onCategoryLoadFailed(message:String?)
}