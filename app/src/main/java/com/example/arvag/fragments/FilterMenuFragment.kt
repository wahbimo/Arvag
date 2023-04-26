package com.example.arvag.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.arvag.R
import com.example.arvag.activities.MainActivity
import com.google.android.material.button.MaterialButton

class FilterMenuFragment : DialogFragment() {

    private lateinit var sortByAlphaButtonAZ: Button
    private lateinit var sortByAlphaButtonZA: Button
    private lateinit var sortByEcoScoreButton: Button
    private lateinit var filterByNutriScoreButton: Button

    private var onFilterIconChangedListener: OnFilterIconChangedListener? = null
    private var onFilterButtonsListener: OnClickFilterButtons? = null


    private var listener: OnFilterIconChangedListener? = null

    private var listenerButtons:OnClickFilterButtons? = null

    fun setListener(listener: OnFilterIconChangedListener) {
        this.listener = listener
    }
    fun setListenerButtons(listener: OnClickFilterButtons) {
        this.listenerButtons = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortByAlphaButtonAZ = view.findViewById(R.id.sort_by_alpha_az_button)
        sortByAlphaButtonZA = view.findViewById(R.id.sort_by_alpha_za_button)
        sortByEcoScoreButton = view.findViewById(R.id.sort_by_ecoscore_button)
        filterByNutriScoreButton = view.findViewById(R.id.filter_by_nutriscore_button)

        // Set click listeners for the buttons
        sortByAlphaButtonAZ.setOnClickListener {
            // Handle click for "Sort by A-Z" button
            listenerButtons?.onSortByAlphaButtonAZ()
            dismiss()
        }

        sortByAlphaButtonZA.setOnClickListener {
            // Handle click for "Sort by Z-A" button
            listenerButtons?.onSortByAlphaButtonZA()
            dismiss()
        }

        sortByEcoScoreButton.setOnClickListener {
            // Handle click for "Sort by Eco Score" button
            listenerButtons?.onSortByEcoScoreButton()
            dismiss()
        }

        filterByNutriScoreButton.setOnClickListener {
            // Handle click for "Filter by Nutri Score" button
            listenerButtons?.onFilterByNutriScoreButton()
            dismiss()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilterIconChangedListener) {
            onFilterIconChangedListener = context
        }
        if (context is OnClickFilterButtons) {
            onFilterButtonsListener = context
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(tag,"destroyed")
        listener?.onFilterIconChanged()
    }


}
interface OnFilterIconChangedListener {
    fun onFilterIconChanged()
}
interface OnClickFilterButtons{
    fun onSortByAlphaButtonAZ()
    fun onSortByAlphaButtonZA()
    fun onSortByEcoScoreButton()
    fun onFilterByNutriScoreButton()
}
