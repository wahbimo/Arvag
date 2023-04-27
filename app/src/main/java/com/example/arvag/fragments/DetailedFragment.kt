package com.example.arvag.fragments
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.arvag.R
import com.example.arvag.products_view.Product


/**
 * A simple [Fragment] subclass.
 * Use the [DetailedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DetailedFragment (private var product:Product) : DialogFragment() {
    private lateinit var productImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var brandTextView: TextView
    private lateinit var categoryTextView: TextView
    //private lateinit var ingredientsTextView: TextView
    private lateinit var nutriImageView: ImageView
    private lateinit var ecoImageView: ImageView
    private lateinit var quitFragment: ImageView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeVariables()
        //product = requireActivity().intent.getParcelableExtra("productItem")!!
        if (product != null){
            setDataToWidgets()
        }
        quitFragment.setOnClickListener{
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed, container, false)
    }

    private fun initializeVariables() {
        productImageView = requireView().findViewById(R.id.detailActivityProductIV)
        nameTextView = requireView().findViewById(R.id.detailProductNameTv)
        brandTextView = requireView().findViewById(R.id.detailBrandTv)
        categoryTextView = requireView().findViewById(R.id.detailActivityCategoryNameTv)
        //ingredientsTextView = requireView().findViewById(R.id.IngredientsProduct)
        nutriImageView = requireView().findViewById(R.id.nutri_image)
        ecoImageView = requireView().findViewById(R.id.eco_image)
        quitFragment = requireView().findViewById(R.id.closeButton)
    }

    private fun setDataToWidgets() {
        nameTextView.text = product.name
        brandTextView.text = product.brand
        categoryTextView.text = product.category
        //ingredientsTextView.text = product.ingredients
        context?.let {
            Glide.with(it)
                .load("file:///android_asset/imagesDB/image${product.id}.jpg")
                .error(R.drawable.baseline_image_not_supported_24)
                .into(productImageView!!)
        }
        val myEcoDrawable = "eco_${product.ecoscore_grade}"

        context?.let {
            Glide.with(it)
                .load(resources.getIdentifier(myEcoDrawable, "drawable", it.packageName))
                .error(R.drawable.eco_n)
                .into(ecoImageView!!)
        }
        val myNutriDrawable = "nutri_score_${product.nutriscore_grade}"
        context?.let {
            Glide.with(it)
                .load(resources.getIdentifier(myNutriDrawable, "drawable", it.packageName))
                .error(R.drawable.baseline_image_not_supported_24)
                .into(nutriImageView!!)
        }
    }

}
interface onClickDetailed {
    fun onClickDetailedFragment()
}