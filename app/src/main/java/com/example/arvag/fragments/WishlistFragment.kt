package com.example.arvag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvag.R
import com.example.arvag.adapter.OnHeartClick
import com.example.arvag.adapter.ProductItemAdapter
import com.example.arvag.adapter.ProductWishlistAdapter
import com.example.arvag.database.ProductDataBase
import com.example.arvag.listener.IRecyclerClickListener
import com.example.arvag.utils.SpaceItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishlistFragment : Fragment(), OnHeartClick {
    private lateinit var productsAdapter: ProductWishlistAdapter
    private lateinit var recyclerProductView: RecyclerView
    private lateinit var deleteButton: ImageView

    private lateinit var heartListener : OnHeartClick


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)
        recyclerProductView = view.findViewById(R.id.recycler_product_wishlist)
        deleteButton = view.findViewById(R.id.delete_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

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

