package com.example.arvag.fragments
import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import com.example.arvag.*
import com.example.arvag.R
import com.example.arvag.maps_compnanions.LocationItem
import com.example.arvag.maps_compnanions.MarkerInfoWindowAdapter
import com.example.arvag.maps_compnanions.PlaceRenderer
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


class MapsFragment() : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var marker_on = false
    private var marker: Marker? = null
    private var circle: Circle? = null
    //private lateinit var suggestions:Array<String>

    companion object{
        const val LOCATION_REQUEST_CODE = 1
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private val callback = OnMapReadyCallback { googleMap ->
        try {
            mMap = googleMap
            googleMap.setPadding(0,0,0,0)
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
            val portsList: ArrayList<LocationItem> = ArrayList()


            val objSuperU = JSONObject(getJSONFromAssets("superu.json")!!)
            val objBiocoop = JSONObject(getJSONFromAssets("biocoop.json")!!)
            val objBelleIloise = JSONObject(getJSONFromAssets("belle_iloise.json")!!)
            val objPorts = JSONObject(getJSONFromAssets("ports_data.json")!!)

            // fetch JSONArray named locationArray... by using getJSONArray
            val locationArraySuperU = objSuperU.getJSONArray("lll")
            val locationArrayBiocoop = objBiocoop.getJSONArray("bbb")
            val locationArrayBelleIloise = objBelleIloise.getJSONArray("bbb")
            val portsArray = objPorts.getJSONArray("ports")

            makingLocationsList(locationArraySuperU, locationsListSuperU)
            makingLocationsList(locationArrayBiocoop, locationsListBiocoop)
            makingLocationsList(locationArrayBelleIloise, locationsListBelleIloise)
            makingLocationsList(portsArray, portsList)
            placeMarkers(portsList,googleMap)
            //addClusteredMarkers(googleMap,portsList)

            val bounds = LatLngBounds.builder()

            ((locationsListSuperU.plus(locationsListBiocoop)).plus(locationsListBelleIloise)).forEach {
                bounds.include(
                    it.position
                )
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 1000,1600,150))

            addClusteredMarkers(
                googleMap,
                (locationsListSuperU.plus(locationsListBiocoop)).plus(locationsListBelleIloise) as ArrayList<LocationItem>
            )

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
            return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        var searchView:SearchView = view.findViewById(R.id.idSearchView)
        var close_button:ImageView = view.findViewById(androidx.appcompat.R.id.search_close_btn)
        var show_toast = false
        searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text).threshold = 1

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.suggestions, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        val citiesList: ArrayList<String> = ArrayList()
        val objCities = JSONObject(getJSONFromAssets("villes_france.json")!!)
        val citiesArray = objCities.getJSONArray("villes_france")
        suggestionsList(citiesArray,citiesList)

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onQueryTextSubmit(query: String?): Boolean {
                var l:String = searchView.query.toString()

                if (l != null){
                var addressList: List<Address>
                val geocoder = context?.let { Geocoder(it) }
                try{
                    if ( !marker_on ) {
                        if (geocoder != null) {
                            addressList = geocoder.getFromLocationName(l,1) as List<Address>

                            if (addressList.isNotEmpty()){
                                var address:Address = addressList.get(0)
                                var latLng = LatLng(address.latitude,address.longitude)
                                marker =
                                    mMap.addMarker(MarkerOptions().position(latLng).title("Ma recherche").snippet(address.getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))!!
                                    circle = addCircle(mMap,latLng)
                                    Toast.makeText(context,"5 km de rayon",Toast.LENGTH_LONG).show()
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f))
                                marker_on = true
                            }
                            else{
                                if (!show_toast)
                                {
                                    Toast.makeText(context,resources.getString(R.string.address_not_valid),Toast.LENGTH_SHORT).show()
                                show_toast = true
                                }
                            }
                        }
                    }
                }catch (e :IOException ){
                    e.printStackTrace()
                }}
            return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                marker?.remove()
                circle?.remove()
                marker_on = false
                show_toast = false

                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))


                newText?.let {
                    citiesList.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }
                cursorAdapter.changeCursor(cursor)
                return true}

        })

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)
                return true
            }
        })

        close_button.setOnClickListener {
            Log.d(TAG, "Search close button clicked")
            if (marker_on) {
                marker?.remove()
                marker_on = false
            }
            var et:EditText = view.findViewById(androidx.appcompat.R.id.search_src_text)
            et.setText("")
            searchView.setQuery("",false)
            show_toast = false
        }

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
                LOCATION_REQUEST_CODE
            )
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

    private fun placeMarkers(markersList: ArrayList<LocationItem>,googleMap: GoogleMap) {
        val ic: BitmapDescriptor by lazy {
            val color = context?.let { ContextCompat.getColor(it, R.color.blue_boat) }
            BitmapHelper.vectorToBitmap(
                requireContext(),
                R.drawable.baseline_directions_boat_24,
                color!!
            )
        }
        for (i in 0 until markersList.size) {
            val locationItem = markersList.get(i)
            val address = locationItem.Address
            val loc = locationItem.loc
            val latLng = LatLng(locationItem.lat,locationItem.lon)
            val markerOptions = MarkerOptions().position(latLng).icon(ic)
        markerOptions.title(address).snippet(loc)
        googleMap.addMarker(markerOptions)

    }}

    private fun placeMarkerOnMap(currentLatLong: LatLng,googleMap: GoogleMap) {
        val markerOptions = MarkerOptions().position(currentLatLong).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.title("Ma localisation")
        googleMap.addMarker(markerOptions)
    }

    override fun onMapReady(map: GoogleMap) {
    }

    fun suggestionsList(
        citiesArray: JSONArray,
        citiesList: ArrayList<String>
    ) {
        for (i in 0 until citiesArray.length()) {
            val key = citiesArray.getJSONObject(i)
            val city = key.getString("city")
            citiesList.add(city)
        }
    }
    private fun addCircle(googleMap: GoogleMap, latLng: LatLng):Circle {
        val circle = googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(5000.0)
                .fillColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
                .strokeColor(ContextCompat.getColor(requireContext(), R.color.arvag_purp))
        )
        return circle
    }

}