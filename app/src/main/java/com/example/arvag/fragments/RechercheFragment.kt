package com.example.arvag.fragments


import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.CategoryItemAdapter
import com.example.arvag.adapter.CategoryOnClickInterface
import com.example.arvag.adapter.ProductItemAdapter
import com.example.arvag.listener.ICategoryLoadListener
import com.example.arvag.listener.IProductLoadListener
import com.example.arvag.listener.IRecyclerClickListener
import com.example.arvag.products_view.Product
import com.example.arvag.utils.SpaceItemDecoration
import com.google.android.material.button.MaterialButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset



/**

* A fragment that displays a search interface and a list of products.

* It allows users to search for products, filter and sort them based on different criteria.
 */
class RechercheFragment : Fragment(), IProductLoadListener, ICategoryLoadListener,
    CategoryOnClickInterface, IRecyclerClickListener, OnFilterIconChangedListener,OnClickFilterButtons
{

    private lateinit var productsAdapter: ProductItemAdapter
    private lateinit var categoryAdapter: CategoryItemAdapter

    lateinit var productLoadListener: IProductLoadListener
    lateinit var recyclerClickListener: IRecyclerClickListener

    lateinit var categoryLoadListener: ICategoryLoadListener

    lateinit var categoryOnClickListener: CategoryOnClickInterface

    lateinit var recyclerProductView: RecyclerView
    lateinit var categoryView: RecyclerView
    lateinit var searchView:SearchView
    lateinit var filterButton:MaterialButton
    lateinit var drawable: Drawable
    lateinit var downArrow: Drawable
    lateinit var upArrow: Drawable


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
        filterButton = view.findViewById(R.id.filtersButton)
        downArrow = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_keyboard_arrow_down_24)!!
        upArrow = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_keyboard_arrow_up_24)!!
        filterButton.icon = downArrow
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
                if (newText == ""){
                    productsAdapter = ProductItemAdapter(requireContext(), productList!!, recyclerClickListener)
                    recyclerProductView.adapter = productsAdapter
                }
                return false
            }

        })
        close_button.setOnClickListener {
            searchView.setQuery("",false)
            productsAdapter = ProductItemAdapter(requireContext(), productList!!, recyclerClickListener)
            recyclerProductView.adapter = productsAdapter
        }
        filterButton.setOnClickListener{
            val filterMenuFragment = FilterMenuFragment()
            filterMenuFragment.setListener(this) // register the listener
            filterMenuFragment.setListenerButtons(this)
            if (filterButton.icon.equals(downArrow)) {
                Log.d(TAG, "Changing icon to up arrow")
                filterButton.icon = upArrow
                filterMenuFragment.show(parentFragmentManager, "FilterMenuFragment")
            } else {
                Log.d(TAG, "Changing icon to down arrow")
                filterButton.icon = downArrow
            }
        }



    }

    override fun onFilterIconChanged() {
        filterButton.icon = downArrow
    }





    /**

    * Initializes the necessary components and sets up the layout.
     */
    private fun init() {
        productLoadListener = this
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

    /**

    * Retrieves the JSON content from the assets folder.
    * @param fileName The name of the JSON file.
    * @return The JSON content as a string, or null if there was an error.
     */
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
    /**

    * Loads the products from the JSON file.
     */
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

    /**
    * Loads the products from the JSON file.
     */
    private fun loadProductFromJSON() {
        val objProducts = JSONObject(getJSONFromAssets("dataBaseProducts.json")!!)
        makingProductsList(objProducts.getJSONArray("products"), productList)
    }

    /**

    * Creates a list of products from the JSON array.
    * @param productsArray The JSON array containing the product data.
    * @param productsList The list to populate with the product data.
     */
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

    override fun onItemClickListener(view:View?,position:Int) {
        //Toast.makeText(context,productsAdapter.getList()[position].name, Toast.LENGTH_SHORT).show()
        val detailedFragment = DetailedFragment(productsAdapter.getList()[position])
        detailedFragment.show(parentFragmentManager, "DetailedFragment")
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
        productsAdapter = ProductItemAdapter(requireContext(), filteredProductList!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
        Toast.makeText(requireContext(), "Aucun produit trouvé.", Toast.LENGTH_SHORT).show()
    } else {
        // at last we are passing that filtered
        // list to our adapter class.
        productsAdapter = ProductItemAdapter(requireContext(), filteredProductList!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
        //productsAdapter.notifyDataSetChanged()
    }
}

    override fun onSortByAlphaButtonAZ() {

        productsAdapter = ProductItemAdapter(requireContext(), productsAdapter.getList().sortedBy { it.name }!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
    }

    override fun onSortByAlphaButtonZA() {
        productsAdapter = ProductItemAdapter(requireContext(),productsAdapter.getList().sortedByDescending { it.name }!!, recyclerClickListener)

        recyclerProductView.adapter = productsAdapter
    }

    override fun onSortByEcoScoreButton() {
        productsAdapter = ProductItemAdapter(requireContext(),productsAdapter.getList().sortedBy { it.ecoscore_grade }!!, recyclerClickListener)

        recyclerProductView.adapter = productsAdapter
    }

    override fun onFilterByNutriScoreButton() {
        productsAdapter = ProductItemAdapter(requireContext(),productsAdapter.getList().sortedBy { it.nutriscore_score }!!, recyclerClickListener)
        recyclerProductView.adapter = productsAdapter
    }


}
