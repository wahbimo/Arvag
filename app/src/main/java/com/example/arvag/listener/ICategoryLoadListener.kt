package com.example.arvag.listener

interface ICategoryLoadListener {
    fun onCategoryLoadSuccess(categoryList: List<String>?)
    fun onCategoryLoadFailed(message:String?)
}