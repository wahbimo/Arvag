package com.example.arvag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.listener.IRecyclerClickListener

class PartnersAdapter(
        private val context:Context,
        private var list: ArrayList<String>
)
        : RecyclerView.Adapter<PartnersAdapter.MyPartnerViewHolder>()
{
        class MyPartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                View.OnClickListener {

                private var clickListener: IRecyclerClickListener? = null
                fun setClickListener(clickListener: IRecyclerClickListener){
                        this.clickListener = clickListener
                }

                var partnerText: AppCompatButton
                init{
                        partnerText = itemView.findViewById(R.id.btnPartner)
                        itemView.setOnClickListener(this)

                }
                override fun onClick(v: View?) {
                        clickListener!!.onItemClickListener(v,adapterPosition)
                }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPartnerViewHolder {
                return MyPartnerViewHolder(
                        LayoutInflater.from(context)
                                .inflate(R.layout.partner_item, parent, false)
                )
        }

        override fun getItemCount(): Int {
                return list.size
        }

        override fun onBindViewHolder(holder: MyPartnerViewHolder, position: Int) {
                holder.partnerText.text = StringBuilder().append(list[position])
        }


}
