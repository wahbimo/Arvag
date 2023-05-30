package com.example.arvag.maps_compnanions

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.arvag.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
/**
 * Custom InfoWindowAdapter for Google Maps markers.
 * This adapter is responsible for providing custom views for marker info windows.
 */
/**
 * Constructs a new MarkerInfoWindowAdapter with the specified context.
 *
 * @param context The context used to inflate the custom view for the info window.
 */
class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    @SuppressLint("MissingInflatedId")
    override fun getInfoContents(p0: Marker): View? {
        // 1. Get tag
        val place = p0?.tag as? LocationItem ?: return null

        // 2. Inflate view and set title, address, and rating
        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_info_contents, null
        )
        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = place.Address
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = place.loc

        return view
    }

    override fun getInfoWindow(p0: Marker): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }
}
