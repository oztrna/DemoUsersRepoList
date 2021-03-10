package com.oztrna.demoproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oztrna.demoproject.fragment.MyFragmentCommand
import com.oztrna.demoproject.fragment.MyFragmentManager
import com.oztrna.demoproject.fragment.MyFragmentType
import com.oztrna.demoproject.listeners.OnFragmentInteractionListener

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private val myFragmentManager = MyFragmentManager(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myFragmentManager.setCurrentFragmentType(MyFragmentType.MY_FRAGMENT_MAIN_LIST, null, false)
    }

    override fun onFragmentInteraction(command: MyFragmentCommand, argumentData: Bundle?) {
        when(command) {
            MyFragmentCommand.OPEN_DETAIL_PAGE -> {
                myFragmentManager.setCurrentFragmentType(
                    MyFragmentType.MY_FRAGMENT_REPO_DETAIL, argumentData, false)
            }
            MyFragmentCommand.OPEN_FAVORITE_PAGE -> {
                myFragmentManager.setCurrentFragmentType(
                    MyFragmentType.MY_FRAGMENT_FAVORITES, argumentData, false)
            }
            MyFragmentCommand.OPEN_MAIN_PAGE -> {
                myFragmentManager.setCurrentFragmentType(
                    MyFragmentType.MY_FRAGMENT_MAIN_LIST, argumentData, false)
            }
        }
    }
}