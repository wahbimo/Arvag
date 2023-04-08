package com.example.arvag

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.arvag.databinding.ActivityMainBinding
import com.google.android.gms.common.util.WorkSourceUtil.add

/*
private val acceuilFragment = AcceuilFragment()
private val rechercheFragment = RechercheFragment()
private val wishlistFragment = WishlistFragment()
private val projetFragment = ProjetFragment()
private val mapsFragment = MapsFragment()
*/


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var doubleBackToExitPressedOnce = false

    private lateinit var nextFragment:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null){
            nextFragment = supportFragmentManager.getFragment(savedInstanceState, "CurrentFragment")!!
        } else{
            nextFragment = AcceuilFragment()
        }
        replaceFragment(nextFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.acceuil -> {
                    nextFragment = AcceuilFragment()
                }
                R.id.autour_de_moi -> {
                    nextFragment = MapsFragment()
                }
                R.id.ma_wishlist -> {
                    nextFragment = WishlistFragment()
                }
                R.id.recherche -> {
                    nextFragment = RechercheFragment()
                }
                R.id.projet_et_partenaires -> {
                    nextFragment = ProjetFragment()
                }
            }
            replaceFragment(nextFragment)
            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "CurrentFragment", nextFragment);    }


    override fun onBackPressed() {
        doubleBackToExit()
    }


    private fun replaceFragment(fragment: Fragment){

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()

    }


    private fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.double_click_to_exit),
            Toast.LENGTH_SHORT
        ).show()
        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }
}





