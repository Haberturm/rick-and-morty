package com.haberturm.rickandmorty.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    var currentFragmentName: String? = null

    private val SAVED_INSTANCE_KEY = "MY_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val fragmentManager = supportFragmentManager
        currentFragmentName = savedInstanceState?.getString(SAVED_INSTANCE_KEY)
        Log.i("ROTATE", "$currentFragmentName")
        setFragment(getFragmentFromName(currentFragmentName))



        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.characters_menu_item -> {
                    setFragment(CharactersMainFragment())
                    true
                }
                R.id.episodes_menu_item -> {
                    setFragment(EpisodesMainFragment())
                    true
                }
                R.id.locations_menu_item -> {
                    setFragment(LocationsMainFragment())
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
        Log.i("ROTATE", "onSave")
        outState.putString(SAVED_INSTANCE_KEY, currentFragmentName)
    }


    private fun setFragment(fragment: Fragment) {
        currentFragmentName = getNameFromFragment(fragment)

        val ft = supportFragmentManager.beginTransaction()
        ft.apply {
            replace(R.id.fragment_container, fragment)
            setReorderingAllowed(true)
            commit()
        }
    }
}

fun getNameFromFragment(fragment: Fragment): String?{
    return when(fragment){
        is CharactersMainFragment -> NavigationDestinations.CharactersMain.name
        is EpisodesMainFragment -> NavigationDestinations.EpisodesMain.name
        is LocationsMainFragment -> NavigationDestinations.LocationsMain.name
        else -> {
            null
        }
    }
}


fun getFragmentFromName(name: String?): Fragment {
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
        null -> {
            CharactersMainFragment()
        }
        else -> {
            CharactersMainFragment()
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

