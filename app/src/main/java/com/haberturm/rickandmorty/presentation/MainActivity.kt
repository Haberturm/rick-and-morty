package com.haberturm.rickandmorty.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.ActivityMainBinding
import com.haberturm.rickandmorty.presentation.navigation.Navigation
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesMainFragment
import com.haberturm.rickandmorty.presentation.screens.locations.LocationsMainFragment
import com.haberturm.rickandmorty.util.Util.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    private val navigation = Navigation(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RickAndMorty)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) { //значит первый запуск приложения
            setFragment(CharactersMainFragment())
        }
        handleBottomNavigation(binding.bottomNavigation)
        setContentView(binding.root)
    }

    override fun onStart() {
        activityTopAppBarSetUp()
        super.onStart()
    }

    override fun onBackPressed() {
        //означает, что переходим на главный экран
        if (supportFragmentManager.backStackEntryCount == 1){
            activityTopAppBarSetUp()
        }
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("NOT_FIRST_LAUNCH_KEY", true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //означает, что переходим на главный экрвн
        if (supportFragmentManager.backStackEntryCount == 1){
            activityTopAppBarSetUp()
        }
        supportFragmentManager.popBackStack()
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setFragment(fragment: Fragment) {
        navigation.replaceFragment(
            containerId = R.id.container_with_bottom_navigation,
            fragment = fragment
        )
    }

    private fun handleBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
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

    private fun activityTopAppBarSetUp(){
        supportActionBar?.apply {
            title = "Rick and Morty"
            setDisplayHomeAsUpEnabled(false)
        }
    }
}


