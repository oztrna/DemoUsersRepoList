package com.oztrna.demoproject.listeners

import android.os.Bundle
import com.oztrna.demoproject.fragment.MyFragmentCommand

interface OnFragmentInteractionListener {
    fun onFragmentInteraction(command: MyFragmentCommand, argumentData: Bundle? = null)
}