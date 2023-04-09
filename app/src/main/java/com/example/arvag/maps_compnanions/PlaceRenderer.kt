package com.example.arvag.maps_compnanions

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.arvag.BitmapHelper
import com.example.arvag.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Place objects.
 */
class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<LocationItem>
) : DefaultClusterRenderer<LocationItem>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val ic: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.arvag_purp)
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.baseline_shopping_cart_24,
            color
        )
    }

    /**
     * Method called before the cluster item (the marker) is rendered.
     * This is where marker options should be set.
     */
    override fun onBeforeClusterItemRendered(
        item: LocationItem,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.title)
            .position(item.position)
            .icon(ic).snippet(item.loc)
    }

    /**
     * Method called right after the cluster item (the marker) is rendered.
     * This is where properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: LocationItem, marker: Marker) {
        marker.tag = clusterItem
    }
}
