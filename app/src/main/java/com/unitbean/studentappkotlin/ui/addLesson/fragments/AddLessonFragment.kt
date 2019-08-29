package com.unitbean.studentappkotlin.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.presenters.AddLessonPresenter
import com.unitbean.studentappkotlin.ui.auth.presenters.AuthPresenter
import com.unitbean.studentappkotlin.ui.auth.views.AddLessonView
import com.unitbean.studentappkotlin.ui.auth.views.AuthView
import com.unitbean.studentappkotlin.ui.lessonsSchedule.fragments.LessonsScheduleFragment
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profileHost.fragments.ProfileHostFragment
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import kotlinx.android.synthetic.main.fragment_add_lesson.*
import kotlinx.android.synthetic.main.fragment_auth_first.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.text.SimpleDateFormat
import java.util.*

class AddLessonFragment : MvpAppCompatFragment(), AddLessonView, View.OnClickListener  {

    @InjectPresenter
    lateinit var presenter: AddLessonPresenter

    private val dayOfTheWeekFormatter: SimpleDateFormat by lazy { SimpleDateFormat("EEEE", Locale.getDefault()) }

    private lateinit var currentDate: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_add_lesson_accept.setOnClickListener(this)
        b_add_lesson_back.setOnClickListener(this)
        tp_add_lesson_start_time.setIs24HourView(true)
        tp_add_lesson_end_time.setIs24HourView(true)
        currentDate = presenter.getCurrentDate()
    }

    override fun onClick(v: View?) {
        when (v) {
            b_add_lesson_accept -> {
                if (et_add_lesson_name.text.toString() == "" || et_add_lesson_teacher_name.text.toString() == "" || et_add_lesson_auditory_number.text.toString() == "") {
                    Snackbar.make(cl_add_lesson_fragment, "Все поля должны быть заполнены", Snackbar.LENGTH_SHORT).show()
                } else {
                    var dayOfTheWeek = ""
                    if (presenter.getPeriod()) dayOfTheWeek = when (dayOfTheWeekFormatter.format(currentDate)){
                        "понедельник" -> "Понедельник"
                        "вторник" -> "Вторник"
                        "среда" -> "Среда"
                        "четверг" -> "Четверг"
                        "пятница" -> "Пятница"
                        "суббота" -> "Суббота"
                        "воскресенье" -> "Воскресенье"
                        else -> "ХЗ"
                    }
                    presenter.registerLesson(
                        LessonModel(
                            et_add_lesson_name.text.toString(),
                            et_add_lesson_teacher_name.text.toString(),
                            et_add_lesson_auditory_number.text.toString(),
                            "" + (if (tp_add_lesson_start_time.currentHour.toString().length == 1) "0" else "") +  tp_add_lesson_start_time.currentHour.toString() + ":" + (if (tp_add_lesson_start_time.currentMinute.toString().length == 1) "0" else "") + tp_add_lesson_start_time.currentMinute,
                            "" + (if (tp_add_lesson_end_time.currentHour.toString().length == 1) "0" else "") + tp_add_lesson_end_time.currentHour.toString() + ":" + "" + (if (tp_add_lesson_end_time.currentMinute.toString().length == 1) "0" else "") + tp_add_lesson_end_time.currentMinute,
                            dayOfTheWeek,
                            currentDate
                        )
                    )

                    presenter.successLogin()
                }
            }
            b_add_lesson_back -> presenter.cancel()
        }

    }
    override fun onRegisterSuccess (isAuth: Boolean){
        if(isAuth) {
            et_add_lesson_name.setText("")
            et_add_lesson_teacher_name.setText("")
            et_add_lesson_auditory_number.setText("")
            (requireActivity() as? MainActivity)?.apply {
                openScreen(LessonsScheduleFragment.TAG, isAddToBackStack = false)
                showNavigationBar(true)
            }
        }
    }

    /*override fun onProfileLoaded(lesson: LessonModel) {
        et_add_lesson_name.setText(lesson.lessonName)
        et_add_lesson_teacher_name.setText(lesson.lessonTeacherName)
        et_add_lesson_auditory_number.setText(lesson.auditoryNumber)
        tp_add_lesson_start_time.currentHour = lesson.lessonStartTime.substringBefore(':').toInt()
        tp_add_lesson_start_time.currentMinute = lesson.lessonStartTime.substringAfter(':').toInt()
        tp_add_lesson_end_time.currentHour = lesson.lessonEndTime.substringBefore(':').toInt()
        tp_add_lesson_end_time.currentMinute = lesson.lessonEndTime.substringAfter(':').toInt()
        //if (lesson.dayOfTheWeek != "")
    }*/

    override fun onCancel() {
        (requireActivity() as? MainActivity)?.apply{
            openScreen(LessonsScheduleFragment.TAG, isAddToBackStack = false)
            showNavigationBar(true)
        }
    }

    companion object {
        val TAG = "AddLessonFragment"
        fun newInstance(): AddLessonFragment {
            return AddLessonFragment()
        }
    }
}