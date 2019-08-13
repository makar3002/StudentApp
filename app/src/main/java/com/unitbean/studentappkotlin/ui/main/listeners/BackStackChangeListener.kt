package com.unitbean.studentappkotlin.ui.main.listeners

import androidx.fragment.app.Fragment

interface BackStackChangeListener {
    fun onBackStackChange(fragment: Fragment)
}