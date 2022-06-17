package com.haberturm.rickandmorty.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.ActivityMainBinding
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.episodes.EpisodesMainFragment
import com.haberturm.rickandmorty.presentation.locations.LocationsMainFragment

class MainActivity : AppCompatActivity() {
    var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val fragmentManager = supportFragmentManager
        setStartFragment(fragmentManager, CharactersMainFragment())



        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.characters_menu_item -> {
                    fragmentManager.commit {
                        replace<CharactersMainFragment>(R.id.fragment_container)
                        setReorderingAllowed(true)
                    }
                    true
                }
                R.id.episodes_menu_item -> {
                    fragmentManager.commit {
                        replace<EpisodesMainFragment>(R.id.fragment_container)
                        setReorderingAllowed(true)
                    }
                    true
                }
                R.id.locations_menu_item -> {
                    fragmentManager.commit {
                        replace<LocationsMainFragment>(R.id.fragment_container)
                        setReorderingAllowed(true)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        setContentView(binding.root)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}

//set the start fragment (in this case: characters fragment)
private fun setStartFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    val ft = fragmentManager.beginTransaction()
    ft.apply {
        replace(R.id.fragment_container, fragment)
        setReorderingAllowed(true)
        commit()
    }
}
fun getFragmentFromName(name: String): Fragment? {
    return when(name){
        NavigationDestinations.CharactersMain.name -> {
            CharactersMainFragment()
        }
        NavigationDestinations.LocationsMain.name -> {
            LocationsMainFragment()
        }
        NavigationDestinations.EpisodesMain.name -> {
            EpisodesMainFragment()
        }
        else -> {
            null
        }
    }
}

sealed class NavigationDestinations{
    object CharactersMain : NavigationDestinations() {
        const val name = "CHARACTERS_MAIN"
    }
    object LocationsMain : NavigationDestinations() {
        const val name = "LOCATIONS_MAIN"
    }
    object EpisodesMain : NavigationDestinations() {
        const val name = "EPISODES_MAIN"
    }
}

