package com.unitbean.studentappkotlin.utils.listeners

import android.view.View

interface BaseClickListener{
    fun onClick(view: View, position: Int)
}