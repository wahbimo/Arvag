package com.example.arvag.activities

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.arvag.*
import com.example.arvag.databinding.ActivityMainBinding
import com.example.arvag.fragments.*

private val accueilFragment = AccueilFragment()
private val rechercheFragment = RechercheFragment()
private val wishlistFragment = WishlistFragment()
private val projetFragment = ProjetFragment()
private val mapsFragment = MapsFragment()
/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var doubleBackToExitPressedOnce = false



    private val fragmentManager = supportFragmentManager

    private var activeFragment: Fragment = accueilFragment

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState The saved instance state.
     */
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


            supportFragmentManager.beginTransaction().apply {
                add(R.id.frame_layout, accueilFragment, getString(R.string.accueil)).hide(accueilFragment)
                add(R.id.frame_layout, rechercheFragment, getString(R.string.recherche)).hide(rechercheFragment)
                add(R.id.frame_layout, wishlistFragment, getString(R.string.wishlist)).hide(wishlistFragment)
                add(R.id.frame_layout, projetFragment, getString(R.string.projet)).hide(projetFragment)
                add(R.id.frame_layout, mapsFragment, getString(R.string.maps)).hide(mapsFragment)
            }.commit()

            fragmentManager.beginTransaction().show(activeFragment).commit()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.accueil -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(accueilFragment).commit()
                    activeFragment = accueilFragment
                    true
                }
                R.id.voisinage -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(mapsFragment).commit()
                    activeFragment = mapsFragment
                    true
                }

                R.id.wishlist -> {
                    //fragmentManager.beginTransaction().replace(R.id.frame_layout, wishlistFragment).commit()
                    fragmentManager.beginTransaction().hide(activeFragment).show(wishlistFragment).commit()
                    activeFragment = wishlistFragment
                    true
                }
                R.id.recherche -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(rechercheFragment).commit()
                    activeFragment = rechercheFragment
                    true
                }
                R.id.projet -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(projetFragment).commit()
                    activeFragment = projetFragment
                    true
                }
                else -> false
            }
        }

    }
    /**
     * Handles the back button press event.
     */
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        doubleBackToExit()
    }

    /**
     * Handles the double back press event to exit the app.
     */
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

