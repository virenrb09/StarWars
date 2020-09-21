package com.viren.ui.usecase

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.transition.*
import com.viren.starwars_ui.R
import com.viren.ui.view.CharacterDetailsFragment
import com.viren.ui.view.CharacterSearchFragment

/**
 * This use case is responsible to perform navigation between fragments or activity
 * so that the activity BL reduces and we can also test the code here with Power Mock,
 * as we have many Android class references
 */
interface NavigationUseCase {
    fun displayCharacterSearchFragment(
        fragmentManager: FragmentManager,
        containerID: Int
    )

    fun launchCharacterDetails(
        activity: Context,
        name: String,
        url: String,
        fragmentManager: FragmentManager,
        containerID: Int
    )
}

class NavigationUseCaseImpl : NavigationUseCase {

    override fun displayCharacterSearchFragment(
        fragmentManager: FragmentManager,
        containerID: Int
    ) {
        val fragment = fragmentManager.findFragmentByTag(CharacterSearchFragment.TAG)
            ?: CharacterSearchFragment.createInstance()
        addFragment(fragmentManager, containerID, fragment, CharacterSearchFragment.TAG, false)
    }

    override fun launchCharacterDetails(
        activity: Context,
        name: String,
        url: String,
        fragmentManager: FragmentManager,
        containerID: Int
    ) {
        addFragment(
            fragmentManager,
            containerID,
            CharacterDetailsFragment.createInstance(name, url),
            CharacterDetailsFragment.TAG,
            true
        )
    }


    private fun addFragment(
        supportFragmentManager: FragmentManager,
        containerID: Int,
        fragment: Fragment,
        tag: String,
        shouldAddToBackStack: Boolean? = true
    ) {
        supportFragmentManager.beginTransaction().run {
            replace(containerID, fragment, tag)
            if (shouldAddToBackStack != false) {
                addToBackStack(tag)
            }
            commit()
        }
    }
}