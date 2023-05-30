package com.example.arvag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.products_view.Product
import com.bumptech.glide.Glide
import com.example.arvag.listener.IRecyclerClickListener

/**
 * Adapter class for the product items in a RecyclerView.
 */

/**
 * Constructor for the ProductItemAdapter.
 *
 * @param context        The context.
 * @param list           The list of product items.
 * @param onClickProduct The click listener for product items.
 */
class ProductItemAdapter (
    private val context:Context,
    private var list:List<Product>,
    private val onClickProduct: IRecyclerClickListener

):RecyclerView.Adapter<ProductItemAdapter.MyProductViewHolder>() {
    /**
     * ViewHolder class for the product item view.
     */
    /**
     * Constructor for the ViewHolder.
     *
     * @param itemView The item view.
     */
    class MyProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView
        var txtName: TextView
        var txtCategory: TextView
        var txtBrand:TextView

        private var clickListener:IRecyclerClickListener? = null

        /**
         * Sets the click listener for the ViewHolder.
         *
         * @param clickListener The click listener.
         */
         fun setClickListener(clickListener: IRecyclerClickListener){
             this.clickListener = clickListener
         }

        init {
            imageView = itemView.findViewById(R.id.productImage) as ImageView
            txtName = itemView.findViewById(R.id.productName) as TextView
            txtCategory = itemView.findViewById(R.id.productCategory) as TextView
            txtBrand = itemView.findViewById(R.id.productBrand) as TextView

            itemView.setOnClickListener(this)
        }
        /**
         * Handles the click event for the product item.
         *
         * @param v The clicked view.
         */
        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v,adapterPosition)
        }
    }
    /**
     * Creates a new ViewHolder object for the product item view.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type.
     * @return The created ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
        return MyProductViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_product_item,parent,false))
    }
    /**
     * Returns the number of items in the list.
     *
     * @return The number of items.
     */
    override fun getItemCount(): Int {
        return list.size
    }
    /**
     * Returns the list of products.
     *
     * @return The list of products.
     */
    fun getList():List<Product>{
        return list
    }
    /**
     * Binds data to the ViewHolder.
     *
     * @param holder   The ViewHolder.
     * @param position The position of the item.
     */
    override fun onBindViewHolder(holder: MyProductViewHolder, position: Int) {
        Glide.with(context)
          .load("file:///android_asset/imagesDB/image${list[position].id}.jpg")
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].name)
        holder.txtCategory!!.text = StringBuilder().append(list[position].category)
        holder.txtBrand!!.text = StringBuilder().append(list[position].brand)
        
        holder.setClickListener(object : IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                onClickProduct.onItemClickListener(view,position)
            }
        })
    }


}