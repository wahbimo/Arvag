package com.example.arvag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.products_view.Product
import com.bumptech.glide.Glide

/**
 * Adapter class for the product items in a wishlist RecyclerView.
 */

/**
 * Constructor for the ProductWishlistAdapter.
 *
 * @param context   The context.
 * @param list      The list of product items.
 * @param onClickH  The click listener for heart button.
 */
class ProductWishlistAdapter (
    private val context: Context,
    private var list: List<Product>,
    private val onClickH: OnHeartClick
): RecyclerView.Adapter<ProductWishlistAdapter.MyProductWishlistViewHolder>() {
    /**
     * ViewHolder class for the product item view in the wishlist.
     */
    /**
     * Constructor for the ViewHolder.
     *
     * @param itemView The item view.
     */
    inner class MyProductWishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView = itemView.findViewById(R.id.productImageWishlist)
        var txtName: TextView = itemView.findViewById(R.id.productNameWishlist)
        var txtCategory: TextView = itemView.findViewById(R.id.productCategoryWishlist)
        var txtBrand: TextView = itemView.findViewById(R.id.productBrandWishlist)
        var buttonHeart: ImageView = itemView.findViewById(R.id.heart_button)

        init {
            buttonHeart.setOnClickListener(this)
        }
        /**
         * Handles the click event for the heart button.
         *
         * @param v The clicked view.
         */
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onClickH.onClickHeart(v, position)
                buttonHeart.setColorFilter(ContextCompat.getColor(context, androidx.appcompat.R.color.material_blue_grey_800), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }
    /**
     * Creates a new ViewHolder object for the product item view in the wishlist.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type.
     * @return The created ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductWishlistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_product_wishlist, parent, false)
        return MyProductWishlistViewHolder(itemView)
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
    override fun onBindViewHolder(holder: MyProductWishlistViewHolder, position: Int) {
        val product = list[position]
        Glide.with(context)
            .load("file:///android_asset/imagesDB/image${product.id}.jpg")
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.imageView)
        holder.txtName.text = product.name
        holder.txtCategory.text = product.category
        holder.txtBrand.text = product.brand
    }
}
/**
 * Interface for handling click events on the heart button in the ProductWishlistAdapter.
 */
interface OnHeartClick {
    /**
     * Called when the heart button is clicked.
     *
     * @param view     The clicked view (heart button).
     * @param position The position of the clicked item in the adapter.
     */
    fun onClickHeart(view: View?, position: Int)
}