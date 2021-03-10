package com.oztrna.demoproject.fragment

import androidx.fragment.app.Fragment

abstract class MyFragment : Fragment() {
    abstract val fragmentType: MyFragmentType
}