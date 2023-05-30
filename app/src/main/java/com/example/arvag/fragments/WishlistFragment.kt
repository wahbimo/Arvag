package com.example.arvag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.OnHeartClick
import com.example.arvag.adapter.ProductWishlistAdapter
import com.example.arvag.database.ProductDataBase
import com.example.arvag.utils.SpaceItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A fragment that displays the user's wishlist of products.
 */
class WishlistFragment : Fragment(), OnHeartClick {
    private lateinit var productsAdapter: ProductWishlistAdapter
    private lateinit var recyclerProductView: RecyclerView
    private lateinit var deleteButton: ImageView

    private lateinit var heartListener : OnHeartClick

    /**
     * Inflates the layout for the fragment and initializes the views.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views
     * @param container          The parent view that the fragment's UI should be attached to
     * @param savedInstanceState The previously saved state of the fragment, or null if unavailable
     * @return The View for the fragment's UI, or null
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)
        recyclerProductView = view.findViewById(R.id.recycler_product_wishlist)
        deleteButton = view.findViewById(R.id.delete_button)
        return view
    }

    /**
     * Called when the view hierarchy is created for the fragment. Initializes the fragment.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @param savedInstanceState The previously saved state of the fragment, or null if unavailable
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    /**
     * Called when the hidden state of the fragment changes. Re-initializes the fragment and handles
     * the delete button click event.
     *
     * @param hidden True if the fragment is now hidden, false otherwise
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        init()
        deleteButton.setOnClickListener{
            lifecycleScope.launch {
                var allProductsInDB = ProductDataBase(requireContext()).productDao().getAllProducts()
                if (allProductsInDB.isNotEmpty()){
                    ProductDataBase(requireContext()).productDao().deleteAllItems()
                }
                allProductsInDB = ProductDataBase(requireContext()).productDao().getAllProducts()
                productsAdapter = ProductWishlistAdapter(requireContext(), allProductsInDB!!,heartListener)
                recyclerProductView.adapter = productsAdapter
            }
        }
    }

    /**
     * Initializes the fragment by setting up the adapter, layout manager, and item decoration for
     * the RecyclerView.
     */
    private fun init(){
        heartListener = this
        val gridLayoutManager = GridLayoutManager(context, 2)
        //productList is the initial list for the ProductsItemAdapter

        lifecycleScope.launch {
            val allProductsInDB = ProductDataBase(requireContext()).productDao().getAllProducts()
            productsAdapter = ProductWishlistAdapter(requireContext(), allProductsInDB!!,heartListener)
            recyclerProductView.adapter = productsAdapter
            recyclerProductView.layoutManager = gridLayoutManager
            recyclerProductView.addItemDecoration(SpaceItemDecoration())
        }


    }

    /**
     * Called when the heart button is clicked on a product item. Deletes the product from the
     * wishlist and updates the RecyclerView.
     *
     * @param view     The View that was clicked (heart button)
     * @param position The position of the clicked item in the RecyclerView
     */
    override fun onClickHeart(view: View?, position: Int) {
        //Toast.makeText(requireContext(),"click",Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val productToDelete = productsAdapter.getList()[position]
            ProductDataBase(requireContext()).productDao().deleteProductItem(productToDelete)
            delay(1000)
            val allProductsInDB = ProductDataBase(requireContext()).productDao().getAllProducts()
            productsAdapter = ProductWishlistAdapter(requireContext(), allProductsInDB!!,heartListener)
            recyclerProductView.adapter = productsAdapter
        }
    }

}

