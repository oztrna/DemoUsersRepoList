package com.oztrna.demoproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.oztrna.demoproject.R
import java.util.*

class MyFragmentManager(private val fragmentManager: FragmentManager) {
    private var currentFragment: MyFragment? = null
    private var baseFragment: MyFragment? = null

    private val myFragmentStack = Stack<MyFragment>()

    fun setCurrentFragmentType(fragmentType: MyFragmentType, arguments: Bundle?, addToBackStack: Boolean) {

        if (currentFragment?.fragmentType === fragmentType) return

        if (!addToBackStack)
            clearBackStack()

        currentFragment = when (fragmentType) {
            MyFragmentType.MY_FRAGMENT_FAVORITES -> FavoritePage()
            MyFragmentType.MY_FRAGMENT_REPO_DETAIL -> DetailPage()
            MyFragmentType.MY_FRAGMENT_MAIN_LIST -> ListingPage()
        }

        currentFragment?.arguments = arguments

        if (addToBackStack) {
            myFragmentStack.add(currentFragment)
        } else {
            baseFragment = currentFragment
        }

        val fragmentTransaction = fragmentManager.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction.add(R.id.fragment_container, currentFragment as Fragment)
                .addToBackStack(fragmentType.toString())
        } else {
            fragmentTransaction.replace(R.id.fragment_container, currentFragment as Fragment)
        }

        try {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isBackStackEmpty(): Boolean {
        return myFragmentStack.size <= 1
    }

    fun clearBackStack() {
        currentFragment = baseFragment
        myFragmentStack.clear()

        if (fragmentManager.backStackEntryCount <= 0) {
            return
        }

        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}