package com.example.arvag.listener

import android.view.View
/**
 * Interface definition for a callback to be invoked when an item in a RecyclerView is clicked.
 */
interface IRecyclerClickListener {

    /**
     * Called when an item in the RecyclerView is clicked.
     *
     * @param view     The View that was clicked
     * @param position The position of the clicked item in the RecyclerView
     */
    fun onItemClickListener(view:View?,position:Int){
    }
}