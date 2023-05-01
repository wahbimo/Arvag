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


class ProductItemAdapter (
    private val context:Context,
    private var list:List<Product>,
    private val onClickProduct: IRecyclerClickListener

):RecyclerView.Adapter<ProductItemAdapter.MyProductViewHolder>() {
    class MyProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView
        var txtName: TextView
        var txtCategory: TextView
        var txtBrand:TextView

        private var clickListener:IRecyclerClickListener? = null
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

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v,adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
        return MyProductViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_product_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun getList():List<Product>{
        return list
    }

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