package com.example.arvag.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 * Use the [RechercheFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class RechercheFragment : Fragment(), IProductLoadListener,ICartLoadListener, ICategoryLoadListener,
    CategoryOnClickInterface, IRecyclerClickListener
{

    private lateinit var productsAdapter: ProductItemAdapter
    private lateinit var categoryAdapter: CategoryItemAdapter

    lateinit var productLoadListener: IProductLoadListener
    lateinit var cartLoadListener: ICartLoadListener
    lateinit var recyclerClickListener: IRecyclerClickListener

    lateinit var categoryLoadListener: ICategoryLoadListener

    lateinit var categoryOnClickListener: CategoryOnClickInterface

    lateinit var recyclerProductView: RecyclerView
    lateinit var categoryView: RecyclerView
    lateinit var searchView:SearchView

    private lateinit var productList: ArrayList<Product>
    private lateinit var categoriesList: ArrayList<String>

    private var filteredProductList: ArrayList<Product> = ArrayList()

    private lateinit var categoryTitle: TextView


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
        searchView = view.findViewById(R.id.idSearch)


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        loadCategoriesFromJSON()
        categoryAdapter.notifyDataSetChanged() //update the recyclerView of categories
        loadProductFromJSON()
        productsAdapter.notifyDataSetChanged() //update the recyclerView of products
        var close_button:ImageView = view.findViewById(androidx.appcompat.R.id.search_close_btn)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText != "") {
                    filter(newText)
                }
                return false
            }

        })
        close_button.setOnClickListener {
            searchView.setQuery("",false)
            productsAdapter = ProductItemAdapter(requireContext(), productList!!, recyclerClickListener)
            recyclerProductView.adapter = productsAdapter
        }

    }





    private fun init() {
        productLoadListener = this
        cartLoadListener = this
        categoryLoadListener = this
        recyclerClickListener = this
        categoryOnClickListener = this

        val gridLayoutManager = GridLayoutManager(context, 2)
        //productList is the initial list for the ProductsItemAdapter
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

    private fun loadProductsFromJSONByCategory(category: String, list: ArrayList<Product>) {
        val objProducts = JSONObject(getJSONFromAssets("dataBaseProducts.json")!!)
        makingProductsListByCategory(objProducts.getJSONArray("products"), list, category)
    }

    private fun makingProductsListByCategory(
        productsArray: JSONArray,
        list: ArrayList<Product>,
        category: String
    ) {
        for (i in 0 until productsArray.length()) {
            val pItem = productsArray.getJSONObject(i)
            val cat = pItem.getString("cat")
            if (cat == category) {

                val id = pItem.getInt("id")
                val name = pItem.getString("name")
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
                list.add(productDetails)
            }
        }
    }

    private fun loadProductFromJSON() {
        val objProducts = JSONObject(getJSONFromAssets("dataBaseProducts.json")!!)
        makingProductsList(objProducts.getJSONArray("products"), productList)
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
    }

    override fun onLoadCartFailed(message: String?) {
        view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onItemClickListener(view:View?,position:Int) {
        Toast.makeText(context,productsAdapter.getList()[position].name, Toast.LENGTH_SHORT).show()
    }

    private fun loadCategoriesFromJSON() {
        val objCategory = JSONObject(getJSONFromAssets("categories.json")!!)
        makingCategoriesList(objCategory.getJSONArray("cat"), categoriesList)
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
        //val adapter = CategoryItemAdapter(requireContext(), categoryList!!, this)
        //categoryView.adapter = adapter
    }

    override fun onCategoryLoadFailed(message: String?) {
        /*view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }*/
    }

    override fun onProductLoadSuccess(productModelList: List<Product>?) {
        //val adapter = ProductItemAdapter(requireContext(), productModelList!!, recyclerClickListener)
        //recyclerProductView.adapter = adapter
    }

    override fun onProductLoadFailed(message: String?) {
        /*view?.let {

            Snackbar.make(it, message!!, Snackbar.LENGTH_LONG).show()

        }*/
    }

    override fun onClickCategory(string: String) {

        productsAdapter = ProductItemAdapter(requireContext(), productList!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter

        searchView.setQuery("",false)
        // Hiding the keyboard
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken  , 0)
        //Setting the name of the current category to the clicked category
        categoryTitle.text = string
        //Scrolling to the top
        (recyclerProductView.layoutManager as GridLayoutManager).scrollToPosition(0)

        productList.clear()
        if (string != "Tout"){
            loadProductsFromJSONByCategory(string,productList)
        }
        else{
            loadProductFromJSON()
        }
        productsAdapter.notifyDataSetChanged()

        }
    private fun filter(text: String) {

    // creating a new array list to filter our data.
    filteredProductList.clear()

    // running a for loop to compare elements.
    for (item in productList) {
        // checking if the entered string matched with any item of our recycler view.
        if (item.name?.toLowerCase()?.contains(text.toLowerCase()) == true){
            filteredProductList.add(item)
            //if (category == "Tout"){filteredProductList.add(item)}
            //if (item.category == category){filteredProductList.add(item)}
        }
        }
    if (filteredProductList.isEmpty()) {
        // if no item is added in filtered list we are
        // displaying a toast message as no data found.
        Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
    } else {
        // at last we are passing that filtered
        // list to our adapter class.
        productsAdapter = ProductItemAdapter(requireContext(), filteredProductList!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
        //productsAdapter.notifyDataSetChanged()
    }
}

}
