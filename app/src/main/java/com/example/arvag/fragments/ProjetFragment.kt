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
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.PartnersAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset


class ProjetFragment : Fragment() {
    lateinit var moreInfoButton: Button
    lateinit var recyclerPartnersView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_projet, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreInfoButton = view.findViewById(R.id.button2)
        recyclerPartnersView = view.findViewById(R.id.recycler_partners)
        moreInfoButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/"))
            startActivity(browserIntent)
        }

        val gridLayoutManager = GridLayoutManager(context, 1)

        val objPartners = JSONObject(getJSONFromAssets("partenaires.json")!!)

        val partnersList: ArrayList<String> = ArrayList()
        makingPartnersList(objPartners.getJSONArray("part"), partnersList)

        val partnersAdapter = PartnersAdapter(requireContext(), partnersList!!)
        recyclerPartnersView.adapter = partnersAdapter
        recyclerPartnersView.layoutManager = gridLayoutManager


    }

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

