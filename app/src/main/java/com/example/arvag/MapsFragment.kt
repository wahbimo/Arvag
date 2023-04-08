package com.example.arvag

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MapsFragment : Fragment() {

    private lateinit var lastLocation: android.location.Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private val callback = OnMapReadyCallback { googleMap ->
        try {

            setUpMap(googleMap)
            googleMap.uiSettings.setZoomControlsEnabled(true)
            // As we have JSON object, so we are getting the object
            //Here we are calling a Method which is returning the JSON object

            val ic: BitmapDescriptor by lazy {
                val color = context?.let { ContextCompat.getColor(it, R.color.arvag_purp) }
                BitmapHelper.vectorToBitmap(
                    requireContext(),
                    R.drawable.baseline_shopping_cart_24,
                    color!!
                )
            }

            val locationsListSuperU: ArrayList<LocationItem> = ArrayList()
            val locationsListBiocoop: ArrayList<LocationItem> = ArrayList()
            val locationsListBelleIloise: ArrayList<LocationItem> = ArrayList()

            val objSuperU = JSONObject(getJSONFromAssets("superu.json")!!)
            val objBiocoop = JSONObject(getJSONFromAssets("biocoop.json")!!)
            val objBelleIloise = JSONObject(getJSONFromAssets("belle_iloise.json")!!)
            // fetch JSONArray named locationArray... by using getJSONArray
            val locationArraySuperU = objSuperU.getJSONArray("lll")
            val locationArrayBiocoop = objBiocoop.getJSONArray("bbb")
            val locationArrayBelleIloise = objBelleIloise.getJSONArray("bbb")

            makingLocationsList(locationArraySuperU, locationsListSuperU)
            makingLocationsList(locationArrayBiocoop, locationsListBiocoop)
            makingLocationsList(locationArrayBelleIloise, locationsListBelleIloise)

            val bounds = LatLngBounds.builder()

            ((locationsListSuperU.plus(locationsListBiocoop)).plus(locationsListBelleIloise)).forEach { bounds.include(it.position) }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 150))

            addClusteredMarkers(googleMap, (locationsListSuperU.plus(locationsListBiocoop)).plus(locationsListBelleIloise) as ArrayList<LocationItem>)
            //googleMap.setOnMapLoadedCallback {}

        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_voisinage, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }


    fun getJSONFromAssets(fileName: String): String? {

        var json: String?
        val charset: Charset = Charsets.UTF_8
        try {
            val myUsersJSONFile = context?.assets?.open(fileName)
            val size = myUsersJSONFile?.available()
            val buffer = size?.let { ByteArray(it) }
            if (myUsersJSONFile != null) {
                myUsersJSONFile.read(buffer)
                myUsersJSONFile.close()

            }

            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun makingLocationsList(
        locationArray: JSONArray,
        locationsList: ArrayList<LocationItem>
    ) {
        for (i in 0 until locationArray.length()) {
            // Create a JSONObject for fetching single User's Data
            val loc = locationArray.getJSONObject(i)
            // Fetch id store it in variable
            val id = loc.getInt("id")
            val address = loc.getString("Address")
            val lat = loc.getDouble("lat")
            val lon = loc.getDouble("lon")
            val l = loc.getString("loc")

            //val p = LatLng(lat, lon)


            // Now add all the variables to the data model class and the data model class to the array list.
            val locDetails = LocationItem(address, id, lat, l, lon)
            // add the details in the list
            locationsList.add(locDetails)
        }
    }

    private fun addClusteredMarkers(googleMap: GoogleMap, locationsList: ArrayList<LocationItem>) {
        // Create the ClusterManager class and set the custom renderer.
        val clusterManager = ClusterManager<LocationItem>(requireContext(), googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                requireContext(),
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))

        // Add the places to the ClusterManager.
        clusterManager.addItems(locationsList)
        clusterManager.cluster()

        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }
        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque.
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that reclustering
            // can be performed when the camera stops moving.
            clusterManager.onCameraIdle()
        }
    }
    //@RequiresApi(Build.VERSION_CODES.N)
    private fun setUpMap(googleMap: GoogleMap){

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE)
            return
        }

        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener{
            if (it != null) {
                lastLocation = it
                val currentLatLong = LatLng(it.latitude,it.longitude)
                placeMarkerOnMap(currentLatLong,googleMap)
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,8f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng,googleMap: GoogleMap) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("Ma localisation")
        googleMap.addMarker(markerOptions)
    }
}


