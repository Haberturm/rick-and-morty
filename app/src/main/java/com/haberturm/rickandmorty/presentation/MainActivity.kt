package com.haberturm.rickandmorty.presentation

import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.ActivityMainBinding
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesMainFragment
import com.haberturm.rickandmorty.presentation.screens.locations.LocationsMainFragment
import com.haberturm.rickandmorty.util.Const.FRAGMENT_KEY
import com.haberturm.rickandmorty.util.Util.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    var currentFragmentName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        currentFragmentName = savedInstanceState?.getString(FRAGMENT_KEY)
        setFragment(getFragmentFromName(currentFragmentName))
        handleBottomNavigation(binding.bottomNavigation)
        setContentView(binding.root)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FRAGMENT_KEY, currentFragmentName)
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

    private fun handleBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnItemSelectedListener {
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
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }
}

fun getNameFromFragment(fragment: Fragment): String?{
    return when(fragment){
        is CharactersMainFragment -> NavigationDestinations.CharactersMain.name
        is EpisodesMainFragment -> NavigationDestinations.EpisodesMain.name
        is LocationsMainFragment -> NavigationDestinations.LocationsMain.name
        else -> { // in case of error currentFragmentName will set to null, so after that it will navigate to character fragment
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
        null -> {//mean saved instance state is empty or some err appear in all these case we should navigate to characters fragment
            CharactersMainFragment()
        }
        else -> {// default value
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

