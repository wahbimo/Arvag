package com.example.arvag.listener

import com.example.arvag.products_view.CartModel

interface ICartLoadListener {
    fun onLoadCartSuccess(cartModelList:List<CartModel>)
    fun onLoadCartFailed(message:String?)
}