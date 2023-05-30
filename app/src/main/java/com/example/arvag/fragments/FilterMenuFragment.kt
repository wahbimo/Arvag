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
/**
 * A dialog fragment that displays a filter menu with various sorting and filtering options.
 */
class FilterMenuFragment : DialogFragment() {

    /**
     * Button for sorting the items in ascending order.
     */
    private lateinit var sortByAlphaButtonAZ: Button

    /**
     * Button for sorting the items in descending order.
     */
    private lateinit var sortByAlphaButtonZA: Button

    /**
     * Button for sorting the items by eco score.
     */
    private lateinit var sortByEcoScoreButton: Button

    /**
     * Button for filtering the items by nutri score.
     */
    private lateinit var filterByNutriScoreButton: Button

    /**
     * Listener for filter icon changes.
     */
    private var onFilterIconChangedListener: OnFilterIconChangedListener? = null

    /**
     * Listener for filter button clicks.
     */
    private var onFilterButtonsListener: OnClickFilterButtons? = null

    /**
     * Listener for filter icon changes.
     */
    private var listener: OnFilterIconChangedListener? = null

    /**
     * Listener for filter button clicks.
     */
    private var listenerButtons:OnClickFilterButtons? = null

    /**
     * Sets the listener for filter icon changes.
     *
     * @param listener The listener to set.
     */
    fun setListener(listener: OnFilterIconChangedListener) {
        this.listener = listener
    }

    /**
     * Sets the listener for filter buttons clicks.
     *
     * @param listener The listener to set.
     */
    fun setListenerButtons(listener: OnClickFilterButtons) {
        this.listenerButtons = listener
    }

    /**
     * Called when the fragment view is created.
     *
     * @param inflater           The layout inflater.
     * @param container          The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The created view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_menu, container, false)
    }

    /**
     * Called when the fragment view is created and ready to be used.
     *
     * @param view               The created view.
     * @param savedInstanceState The saved instance state.
     */
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

    /**
     * Called when the fragment is attached to a context.
     *
     * @param context The context to which the fragment is attached.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilterIconChangedListener) {
            onFilterIconChangedListener = context
        }
        if (context is OnClickFilterButtons) {
            onFilterButtonsListener = context
        }

    }

    /**
     * Called when the fragment view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(tag,"destroyed")
        listener?.onFilterIconChanged()
    }


}
/**
 * Listener interface for filter icon changes.
 */
interface OnFilterIconChangedListener {
    fun onFilterIconChanged()
}

/**
 * Listener interface for filter button clicks.
 */
interface OnClickFilterButtons{
    fun onSortByAlphaButtonAZ()
    fun onSortByAlphaButtonZA()
    fun onSortByEcoScoreButton()
    fun onFilterByNutriScoreButton()
}
