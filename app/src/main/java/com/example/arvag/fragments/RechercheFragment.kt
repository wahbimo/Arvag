package com.example.arvag.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.CategoryItemAdapter
import com.example.arvag.adapter.CategoryOnClickInterface
import com.example.arvag.adapter.ProductItemAdapter
import com.example.arvag.listener.ICartLoadListener
import com.example.arvag.listener.ICategoryLoadListener
import com.example.arvag.listener.IProductLoadListener
import com.example.arvag.listener.IRecyclerClickListener
import com.example.arvag.products_view.CartModel
import com.example.arvag.products_view.Product
import com.example.arvag.utils.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import org.json.JSONArray

import java.io.IOException
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 * Use the [RechercheFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class RechercheFragment : Fragment(), IProductLoadListener,ICartLoadListener, ICategoryLoadListener,
    CategoryOnClickInterface,
    IRecyclerClickListener {

    private lateinit var productsAdapter: ProductItemAdapter
    private lateinit var categoryAdapter: CategoryItemAdapter

    lateinit var productLoadListener: IProductLoadListener
    lateinit var cartLoadListener: ICartLoadListener
    lateinit var recyclerClickListener: IRecyclerClickListener

    lateinit var categoryLoadListener: ICategoryLoadListener

    lateinit var categoryOnClickListener: CategoryOnClickInterface

    lateinit var recyclerProductView: RecyclerView
    lateinit var categoryView: RecyclerView

    private lateinit var productList: ArrayList<Product>
    private lateinit var categoriesList: ArrayList<String>

    private lateinit var categoryTitle: TextView


    /*override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         //recycler_product = view?.findViewById(R.id.recycler_product)!!

     }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadCategoriesFromJSON()
        loadProductFromJSON()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productList = ArrayList()
        categoriesList = ArrayList()
        val view = inflater.inflate(R.layout.fragment_recherche, container, false)
        recyclerProductView = view.findViewById(R.id.recycler_product)
        categoryView = view.findViewById(R.id.rvMainCategories)
        categoryTitle = view.findViewById(R.id.tvMainCategories)
        /*init()
        loadCategoriesFromJSON()
        loadProductFromJSON()*/

        // Inflate the layout for this fragment
        return view //inflater.inflate(R.layout.fragment_recherche, container, false)
    }


    private fun init() {
        productLoadListener = this
        cartLoadListener = this
        categoryLoadListener = this
        recyclerClickListener = this
        categoryOnClickListener = this

        val gridLayoutManager = GridLayoutManager(context, 2)
        productsAdapter = ProductItemAdapter(requireContext(), productList!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
        recyclerProductView.layoutManager = gridLayoutManager
        recyclerProductView.addItemDecoration(SpaceItemDecoration())


        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        categoryAdapter = CategoryItemAdapter(requireContext(),categoriesList, categoryOnClickListener)
        categoryView.adapter = categoryAdapter
        categoryView.layoutManager = categoryLayoutManager
    }


    fun getJSONFromAssets(fileName: String): String? {

        var json: String?
        val charset: Charset = Charsets.UTF_8
        try {
            val myUsersJSONFile = context?.assets?.open(fileName)
            val size = myUsersJSONFile?.available()
            val buffer = size?.let { ByteArray(it) }
            if (myUsersJSONFile != null) {
                myUsersJSONFile.read(buffer)
                myUsersJSONFile.close()

            }

            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun loadProductsFromJSONByCategory(category: String, productsList: ArrayList<Product>) {
        val objProducts = JSONObject(getJSONFromAssets("dataBaseProducts.json")!!)
        //val productModels: ArrayList<Product> = ArrayList()
        makingProductsListByCategory(objProducts.getJSONArray("products"), productsList, category)
        //productList = productModels
        /*if (productModels.isNotEmpty()) {
            productLoadListener.onProductLoadSuccess(productModels)
        } else {
            productLoadListener.onProductLoadFailed("Les produits n'existe pas!")
        }*/
    }

    private fun makingProductsListByCategory(
        productsArray: JSONArray,
        productsList: ArrayList<Product>,
        category: String
    ) {
        for (i in 0 until productsArray.length()) {
            val pItem = productsArray.getJSONObject(i)
            val cat = pItem.getString("cat")
            if (cat == category) {
                // Create a JSONObject for fetching single User's Data
                //val pItem = productsArray.getJSONObject(i)
                // Fetch id store it in variable
                val id = pItem.getInt("id")
                val name = pItem.getString("name")
                //val cat = pItem.getString("cat")
                val brand = pItem.getString("brands")
                val categories = pItem.getString("categories")
                val allergens = pItem.getString("allergens")
                val ecoscore_grade = pItem.getString("ecoscore_grade")
                val nutriscore_grade = pItem.getString("nutriscore_grade")
                val ingredients = pItem.getString("ingredients")
                val quantity = pItem.getString("quantity")
                val packaging = pItem.getString("packaging")
                val ecoscore_score = pItem.getString("ecoscore_score")
                val nutriscore_score = pItem.getString("nutriscore_score")
                //val photos = pItem.getInt("id")


                // Now add all the variables to the data model class and the data model class to the array list.
                val productDetails = Product(
                    id,
                    name,
                    cat,
                    brand,
                    categories,
                    allergens,
                    ecoscore_grade,
                    nutriscore_grade,
                    ingredients,
                    quantity,
                    packaging,
                    ecoscore_score,
                    nutriscore_score
                )
                // add the details in the list
                productsList.add(productDetails)
            }
        }
    }

    private fun loadProductFromJSON() {
        val objProducts = JSONObject(getJSONFromAssets("dataBaseProducts.json")!!)
        //val productModels: ArrayList<Product> = ArrayList()
        makingProductsList(objProducts.getJSONArray("products"), productList)
        productsAdapter.notifyDataSetChanged()
        //productList = productModels
        /*if (productModels.isNotEmpty()) {
            productLoadListener.onProductLoadSuccess(productModels)
        } else {
            productLoadListener.onProductLoadFailed("Les produits n'existe pas!")
        }*/
    }

    fun makingProductsList(
        productsArray: JSONArray,
        productsList: ArrayList<Product>
    ) {
        for (i in 0 until productsArray.length()) {
            // Create a JSONObject for fetching single User's Data
            val pItem = productsArray.getJSONObject(i)
            // Fetch id store it in variable
            val id = pItem.getInt("id")
            val name = pItem.getString("name")
            val cat = pItem.getString("cat")
            val brand = pItem.getString("brands")
            val categories = pItem.getString("categories")
            val allergens = pItem.getString("allergens")
            val ecoscore_grade = pItem.getString("ecoscore_grade")
            val nutriscore_grade = pItem.getString("nutriscore_grade")
            val ingredients = pItem.getString("ingredients")
            val quantity = pItem.getString("quantity")
            val packaging = pItem.getString("packaging")
            val ecoscore_score = pItem.getString("ecoscore_score")
            val nutriscore_score = pItem.getString("nutriscore_score")
            //val photos = pItem.getInt("id")


            // Now add all the variables to the data model class and the data model class to the array list.
            val productDetails = Product(
                id,
                name,
                cat,
                brand,
                categories,
                allergens,
                ecoscore_grade,
                nutriscore_grade,
                ingredients,
                quantity,
                packaging,
                ecoscore_score,
                nutriscore_score
            )
            // add the details in the list
            productsList.add(productDetails)
        }
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
    }

    override fun onLoadCartFailed(message: String?) {
        view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onItemClickListener(view:View?,position:Int) {
        Toast.makeText(context,productList[position].name, Toast.LENGTH_SHORT).show()
        /*val intent = Intent(context, DetailedFragment::class.java)
        intent.putExtra("productItem", product)
        startActivity(intent)*/
    }

    private fun loadCategoriesFromJSON() {
        val objCategory = JSONObject(getJSONFromAssets("categories.json")!!)
        //val categoriesList: ArrayList<String> = ArrayList()
        makingCategoriesList(objCategory.getJSONArray("cat"), categoriesList)
        categoryAdapter.notifyDataSetChanged()
        /*if (categoriesList.isNotEmpty()) {
            categoryLoadListener.onCategoryLoadSuccess(categoriesList)
        } else {
            categoryLoadListener.onCategoryLoadFailed("Les categories n'existe pas!")
        }*/
    }

    private fun makingCategoriesList(
        categoriesArray: JSONArray,
        categoriesList: ArrayList<String>
    ) {
        categoriesList.add("Tout")
        for (i in 0 until categoriesArray.length()) {
            // Create a JSONObject for fetching single User's Data
            val cItem = categoriesArray.getJSONObject(i)
            // Fetch id store it in variable
            val category = cItem.getString("cat")

            categoriesList.add(category)
        }
    }

    override fun onCategoryLoadSuccess(categoryList: List<String>?) {
        val adapter = CategoryItemAdapter(requireContext(), categoryList!!, this)
        categoryView.adapter = adapter
    }

    override fun onCategoryLoadFailed(message: String?) {
        view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onProductLoadSuccess(productModelList: List<Product>?) {
        val adapter = ProductItemAdapter(requireContext(), productModelList!!, recyclerClickListener)
        recyclerProductView.adapter = adapter
    }

    override fun onProductLoadFailed(message: String?) {
        view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onClickCategory(string: String) {
        //Toast.makeText(requireContext(),string, Toast.LENGTH_LONG).show()
        categoryTitle.text = string
        (recyclerProductView.layoutManager as GridLayoutManager).scrollToPosition(0)
        var textButton = string
        //Toast.makeText(context,textButton, Toast.LENGTH_LONG).show()
        productList.clear()
        if (textButton != "Tout"){
            loadProductsFromJSONByCategory(textButton.toString(),productList)
        }
        else{
             loadProductFromJSON()
        }
        productsAdapter.notifyDataSetChanged()
        }

    }
