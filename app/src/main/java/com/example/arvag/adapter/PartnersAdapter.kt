package com.example.arvag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.listener.IRecyclerClickListener
/**
 * Adapter class for the partners list in a RecyclerView.
 */
/**
 * Constructor for the PartnersAdapter.
 *
 * @param context The context.
 * @param list    The list of partner items.
 */
class PartnersAdapter(
        private val context:Context,
        private var list: ArrayList<String>
)
        : RecyclerView.Adapter<PartnersAdapter.MyPartnerViewHolder>()
{
        /**
         * ViewHolder class for the partner item view.
         */
        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The item view.
         */
        class MyPartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                View.OnClickListener {

                private var clickListener: IRecyclerClickListener? = null
                /**
                 * Sets the click listener for the ViewHolder.
                 *
                 * @param clickListener The click listener.
                 */
                fun setClickListener(clickListener: IRecyclerClickListener){
                        this.clickListener = clickListener
                }

                var partnerText: AppCompatButton
                init{
                        partnerText = itemView.findViewById(R.id.btnPartner)
                        itemView.setOnClickListener(this)

                }
                /**
                 * Handles the click event for the partner item.
                 *
                 * @param v The clicked view.
                 */
                override fun onClick(v: View?) {
                        clickListener!!.onItemClickListener(v,adapterPosition)
                }
        }


        /**
         * Creates a new ViewHolder object for the partner item view.
         *
         * @param parent   The parent ViewGroup.
         * @param viewType The view type.
         * @return The created ViewHolder.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPartnerViewHolder {
                return MyPartnerViewHolder(
                        LayoutInflater.from(context)
                                .inflate(R.layout.partner_item, parent, false)
                )
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
         * Binds data to the ViewHolder.
         *
         * @param holder   The ViewHolder.
         * @param position The position of the item.
         */
        override fun onBindViewHolder(holder: MyPartnerViewHolder, position: Int) {
                holder.partnerText.text = StringBuilder().append(list[position])
        }


}
