package com.unitbean.studentappkotlin.ui.lessonsSchedule.listeners

import android.view.View

interface TransitionClickListener {
    fun onItemClick(view: View, position: Int, transitions: Map<String, View>?)
}