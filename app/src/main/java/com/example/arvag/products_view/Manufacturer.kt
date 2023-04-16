package com.example.arvag.products_view

import com.example.arvag.maps_compnanions.LocationItem
import com.google.gson.annotations.SerializedName

data class Manufacturer (
    @SerializedName("description")
    var description: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("photo")
    var photos: Photo? = null,

    @SerializedName("location")
    var locations: List<LocationItem> = arrayListOf()
)