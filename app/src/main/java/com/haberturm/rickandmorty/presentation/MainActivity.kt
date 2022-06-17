package com.haberturm.rickandmorty.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.locations.LocationsMainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        setStartFragment(fragmentManager)


    }
}

//set the start fragment (in this case: characters fragment)
private fun setStartFragment(fragmentManager: FragmentManager) {
    fragmentManager.commit {
        replace<LocationsMainFragment>(R.id.fragment_container)
        setReorderingAllowed(true)
    }
}