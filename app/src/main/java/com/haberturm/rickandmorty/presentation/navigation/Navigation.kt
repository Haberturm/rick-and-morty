package com.haberturm.rickandmorty.presentation.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class Navigation(
    private val fragmentManager: FragmentManager
) {


    fun replaceFragment(
        containerId: Int,
        fragment: Fragment,
        arguments: Bundle,
        addToBackStack: String
    ){
        fragmentManager.commit {
            fragment.arguments = arguments
            replace(containerId, fragment)
            if (addToBackStack.isNotEmpty()){
                addToBackStack(addToBackStack)
            }
            setReorderingAllowed(true)
        }
    }
}