package com.example.arvag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.arvag.databinding.ActivityMainBinding

private val acceuilFragment = AcceuilFragment()
private val rechercheFragment = RechercheFragment()
private val wishlistFragment = WishlistFragment()
private val projetFragment = ProjetFragment()
private val mapsFragment = MapsFragment()

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var doubleBackToExitPressedOnce = false



    private val fragmentManager = supportFragmentManager

    private var activeFragment: Fragment = acceuilFragment


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            supportFragmentManager.beginTransaction().apply {
                add(R.id.frame_layout, acceuilFragment, getString(R.string.accueil))
                add(R.id.frame_layout, rechercheFragment, getString(R.string.recherche)).hide(rechercheFragment)
                add(R.id.frame_layout, wishlistFragment, getString(R.string.wishlist)).hide(wishlistFragment)
                add(R.id.frame_layout, projetFragment, getString(R.string.projet)).hide(projetFragment)
                add(R.id.frame_layout, mapsFragment, getString(R.string.maps)).hide(mapsFragment)
            }.commit()


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.acceuil -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(acceuilFragment).commit()
                    activeFragment = acceuilFragment
                    true
                }
                R.id.autour_de_moi -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(mapsFragment).commit()
                    activeFragment = mapsFragment
                    true
                }
                R.id.ma_wishlist -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(wishlistFragment).commit()
                    activeFragment = wishlistFragment
                    true
                }
                R.id.recherche -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(rechercheFragment).commit()
                    activeFragment = rechercheFragment
                    true
                }
                R.id.projet_et_partenaires -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(projetFragment).commit()
                    activeFragment = projetFragment
                    true
                }
                else -> false
            }
        }

    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        doubleBackToExit()
    }

    @Suppress("DEPRECATION")
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





