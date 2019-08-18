package com.unitbean.studentappkotlin.ui.lessonsSchedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.lessonsSchedule.presentsers.LessonsSchedulePresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class LessonsScheduleFragment : MvpAppCompatFragment(), View.OnClickListener  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lessons_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {

    }

    companion object {
        val TAG = "LessonsSheduleFragment"
        fun newInstance(): LessonsScheduleFragment {
            return LessonsScheduleFragment()
        }
    }
}