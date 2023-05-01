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


class ProductWishlistAdapter (
    private val context: Context,
    private var list: List<Product>,
    private val onClickH: OnHeartClick
): RecyclerView.Adapter<ProductWishlistAdapter.MyProductWishlistViewHolder>() {

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

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onClickH.onClickHeart(v, position)
                buttonHeart.setColorFilter(ContextCompat.getColor(context, androidx.appcompat.R.color.material_blue_grey_800), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductWishlistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_product_wishlist, parent, false)
        return MyProductWishlistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun getList():List<Product>{
        return list
    }


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

interface OnHeartClick {
    fun onClickHeart(view: View?, position: Int)
}