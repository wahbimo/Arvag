package com.example.arvag.products_view

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("description")
    var description: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("photos")
    var photos: List<Photo> = arrayListOf(),

    @SerializedName("manufacturer")
    var manufacturer: Manufacturer? = null,

    @SerializedName("retailers")
    var retailers: List<Retailer> = arrayListOf(),

    @SerializedName("category")
    var special: List<String> = arrayListOf()

)
