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


class CategoryItemAdapter(
    private val context: Context,
    private val list:List<String>,
    private val onClickCategory: CategoryOnClickInterface
): RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>(){



    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        //var txtName: TextView
        var btnCategory: AppCompatButton

        private var clickListener: CategoryOnClickInterface? = null
        fun setClickListener(clickListener: CategoryOnClickInterface){
            this.clickListener = clickListener
        }

        init {
            //txtName = itemView.findViewById(R.id.tvMainCategories) as TextView
            btnCategory = itemView.findViewById(R.id.btnItemCategory)

            btnCategory.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener!!.onClickCategory(btnCategory.text as String)
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.upper_category_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnCategory!!.text = StringBuilder().append(list[position])


        holder.setClickListener (object :CategoryOnClickInterface{
            override fun onClickCategory(string: String) {
                onClickCategory.onClickCategory(holder.btnCategory.text as String)

            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }


}

interface CategoryOnClickInterface{
    fun  onClickCategory(string: String){}
}

