package com.example.arvag.fragments
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
import com.bumptech.glide.Glide
import com.example.arvag.R
import com.example.arvag.products_view.Product


/**
 * A simple [Fragment] subclass.
 * Use the [DetailedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DetailedFragment : Fragment() {
    private lateinit var product: Product
    private lateinit var productImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var brandTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var ingredientsTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeVariables()
        product = requireActivity().intent.getParcelableExtra("productItem")!!
        if (product != null){
            setDataToWidgets()
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
        ingredientsTextView = requireView().findViewById(R.id.detailBrandTv)
    }

    private fun setDataToWidgets() {
        nameTextView.text = product.name
        brandTextView.text = product.brand
        categoryTextView.text = product.category
        ingredientsTextView.text = product.ingredients
        context?.let {
            Glide.with(it)
                .load("file:///android_asset/imagesDB/image${product.id}.jpg")
                .error(R.drawable.baseline_image_not_supported_24)
                .into(productImageView!!)
        }
    }
}
