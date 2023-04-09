package com.example.arvag.maps_compnanions

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class LocationItem(
    val Address: String,
    val id: Int,
    val lat: Double,
    val loc: String,
    val lon: Double
): ClusterItem {


    override fun getPosition(): LatLng =
        LatLng(lat,lon)

    override fun getTitle(): String = Address

    override fun getSnippet(): String = loc
}

