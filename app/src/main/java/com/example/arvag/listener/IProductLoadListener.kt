package com.example.arvag.listener

import com.example.arvag.products_view.Product

interface IProductLoadListener {
    fun onProductLoadSuccess(productModelList: List<Product>?)
    fun onProductLoadFailed(message:String?)
}