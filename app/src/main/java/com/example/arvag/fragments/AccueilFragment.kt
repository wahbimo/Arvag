package com.example.arvag.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

import com.example.arvag.R

/**
 * A fragment that displays buttons for accessing more information and contacting.
 */
class AccueilFragment : Fragment() {
    lateinit var moreInfoButton: Button
    lateinit var contactButton: Button
    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing any saved instance state information.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_accueil, container, false)
        return view
    }

    /**
     * Called when the fragment's view has been created.
     *
     * @param view               The inflated View object representing the fragment's UI.
     * @param savedInstanceState A Bundle containing any saved instance state information.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreInfoButton = view.findViewById(R.id.buttonsite)
        moreInfoButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/"))
            startActivity(browserIntent)
        }
        contactButton = view.findViewById(R.id.buttoncontact)
        contactButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/contact/"))
            startActivity(browserIntent)
        }
    }
    }