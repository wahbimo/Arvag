package com.example.arvag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.arvag.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AcceuilFragment())


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.acceuil -> replaceFragment(AcceuilFragment())
                R.id.autour_de_moi -> replaceFragment(MapsFragment())
                R.id.ma_wishlist -> replaceFragment(WishlistFragment())
                R.id.recherche -> replaceFragment(RechercheFragment())
                R.id.projet_et_partenaires -> replaceFragment(ProjetFragment())
            }
            true
        }


    }
    private fun replaceFragment(fragment: Fragment){
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
        }
}





