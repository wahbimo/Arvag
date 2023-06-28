package com.example.arvag.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.PartnersAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

/**
 * A fragment representing the ProjetFragment in the app.
 * This fragment displays a list of partners and provides a button to open a web page.
 */
class ProjetFragment : Fragment() {
    lateinit var moreInfoButton: Button
    lateinit var recyclerPartnersView: RecyclerView

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return                   The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_projet, container, false)
        return view
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreInfoButton = view.findViewById(R.id.button2)
        recyclerPartnersView = view.findViewById(R.id.recycler_partners)
        moreInfoButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/"))
            startActivity(browserIntent)
        }

       val gridLayoutManager = GridLayoutManager(context, 1)
       // val gridLayoutManager = LinearLayoutManager(requireContext())


        val objPartners = JSONObject(getJSONFromAssets("partenaires.json")!!)

        val partnersList: ArrayList<String> = ArrayList()
        makingPartnersList(objPartners.getJSONArray("part"), partnersList)

        val partnersAdapter = PartnersAdapter(requireContext(), partnersList!!)
        recyclerPartnersView.adapter = partnersAdapter
        recyclerPartnersView.layoutManager = gridLayoutManager


    }

    /**
     * Extracts partner names from a JSON array and adds them to the partnersList.
     * @param partnersArray The JSONArray containing partner objects.
     * @param partnersList  The ArrayList to store partner names.
     */
    fun makingPartnersList(
        partnersArray: JSONArray,
        partnersList: ArrayList<String>
    ) {
        for (i in 0 until partnersArray.length()) {
            val pItem = partnersArray.getJSONObject(i)

            val name = pItem.getString("part")

            // add the details in the list
            partnersList.add(name)
        }
    }

    /**
     * Reads JSON data from a file in the assets folder.
     * @param fileName The name of the JSON file.
     * @return         The JSON data as a string, or null if an error occurs.
     */
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



}

