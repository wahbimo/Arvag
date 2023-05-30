package com.example.arvag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R

/**
 * Adapter class for the category item list in a RecyclerView.
 */

/**
 * Constructor for the CategoryItemAdapter.
 *
 * @param context        The context.
 * @param list           The list of category items.
 * @param onClickCategory The onClickCategory interface for item click events.
 */
class CategoryItemAdapter(
    private val context: Context,
    private val list:List<String>,
    private val onClickCategory: CategoryOnClickInterface
): RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>(){


    /**
     * ViewHolder class for the category item view.
     */
    /**
     * Constructor for the ViewHolder.
     *
     * @param itemView The item view.
     */
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        //var txtName: TextView
        var btnCategory: AppCompatButton

        private var clickListener: CategoryOnClickInterface? = null
        /**
         * Sets the click listener for the ViewHolder.
         *
         * @param clickListener The click listener.
         */
        fun setClickListener(clickListener: CategoryOnClickInterface){
            this.clickListener = clickListener
        }

        init {
            //txtName = itemView.findViewById(R.id.tvMainCategories) as TextView
            btnCategory = itemView.findViewById(R.id.btnItemCategory)

            btnCategory.setOnClickListener(this)
        }
        /**
         * Handles the click event for the category button.
         *
         * @param v The clicked view.
         */
        override fun onClick(v: View?) {
            clickListener!!.onClickCategory(btnCategory.text as String)
        }


    }
    /**
     * Creates a new ViewHolder object for the category item view.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type.
     * @return The created ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.upper_category_button, parent, false)
        )
    }
    /**
     * Binds data to the ViewHolder.
     *
     * @param holder   The ViewHolder.
     * @param position The position of the item.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnCategory!!.text = StringBuilder().append(list[position])


        holder.setClickListener (object :CategoryOnClickInterface{
            override fun onClickCategory(string: String) {
                onClickCategory.onClickCategory(holder.btnCategory.text as String)

            }
        })

    }

    /**
     * Returns the number of items in the list.
     *
     * @return The number of items.
     */
    override fun getItemCount(): Int {
        return list.size
    }


}
/**
 * Interface for handling category item click events.
 */
interface CategoryOnClickInterface{
    /**
     * Called when a category item is clicked.
     *
     * @param string The clicked category item.
     */
    fun  onClickCategory(string: String){}
}

