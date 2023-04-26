package com.example.arvag.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.arvag.dao.CartDAO

import com.example.arvag.database.CartDatabase
import com.example.arvag.products_view.ProductCart
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CartRepo(application: Application?) {
    private val cartDAO: CartDAO?
    private val allCartItemsLiveData: LiveData<List<ProductCart?>?>?
    private val executor: Executor = Executors.newSingleThreadExecutor()
    fun getAllCartItemsLiveData(): LiveData<List<ProductCart?>?>? {
        return allCartItemsLiveData
    }

    init {
        cartDAO = CartDatabase.getInstance(application!!)!!.cartDAO()
        allCartItemsLiveData = cartDAO!!.allCartItems
    }

    fun insertCartItem(productCart: ProductCart?) {
        executor.execute { cartDAO!!.insertCartItem(productCart) }
    }

    fun deleteCartItem(productCart: ProductCart?) {
        executor.execute { cartDAO!!.deleteCartItem(productCart) }
    }

    fun deleteAllCartItems() {
        executor.execute { cartDAO!!.deleteAllItems() }
    }
}