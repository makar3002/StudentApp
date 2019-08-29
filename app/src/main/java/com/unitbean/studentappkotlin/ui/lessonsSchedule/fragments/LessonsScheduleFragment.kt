package com.unitbean.studentappkotlin.ui.lessonsSchedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.fragments.AddLessonFragment
import com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters.DaysRVAdapter
import com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters.LessonsRVAdapter
import com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters.MonthsRVAdapter
import com.unitbean.studentappkotlin.ui.lessonsSchedule.itemDecorations.OffsetItemDecoration
import com.unitbean.studentappkotlin.ui.lessonsSchedule.listeners.TransitionClickListener
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.DateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.IDateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.ILessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.LessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.presentsers.LessonsSchedulePresenter
import com.unitbean.studentappkotlin.ui.lessonsSchedule.views.LessonsScheduleView
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.main.listeners.BackStackChangeListener
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_lessons_schedule.*
import kotlinx.coroutines.plus
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class LessonsScheduleFragment : MvpAppCompatFragment(), LessonsScheduleView, TransitionClickListener  {

    @InjectPresenter lateinit var presenter: LessonsSchedulePresenter

    private val daysAdapter: DaysRVAdapter by lazy {
        DaysRVAdapter().apply {
            this.transitionClickListener = this@LessonsScheduleFragment
        }
    }

    private val lessonsAdapter: LessonsRVAdapter by lazy {
        LessonsRVAdapter().apply {
            this.transitionClickListener = this@LessonsScheduleFragment
        }
    }

    private val monthsAdapter: MonthsRVAdapter by lazy {
        MonthsRVAdapter().apply {
            this.transitionClickListener = this@LessonsScheduleFragment
        }
    }

    private val snapHelper: LinearWithoutDecorationsSnapHelper = LinearWithoutDecorationsSnapHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lessons_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pb_lessons_empty.visibility = View.VISIBLE
        tv_lessons_empty.visibility = View.GONE
        bs_rv_container.visibility = View.GONE
        rv_date_line.setHasFixedSize(true)
        rv_date_line.addItemDecoration(OffsetItemDecoration(activity))
        snapHelper.attachToRecyclerView(rv_date_line)
        rv_date_line.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_date_line.adapter = daysAdapter

        rv_month_line.setHasFixedSize(true)
        rv_month_line.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_month_line.adapter = monthsAdapter

        rv_lessons_list.setHasFixedSize(true)
        rv_lessons_list.adapter = lessonsAdapter
        srl_lessons_updater.setOnRefreshListener {
            presenter.loadLessons()
        }

        postponeEnterTransition()

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
            (activity as? BackStackChangeListener)?.onBackStackChange(this)
        }

        presenter.loadDays()
    }



    override fun onItemClick(view: View, position: Int, transitions: Map<String, View>?) {
        when (view.id) {
            R.id.ll_date_item -> {
                val model = presenter.dates[position] as DateViewModel
                (snapHelper as SnapHelper).smoothScrollToPosition(position)
                presenter.setCurrentDate(model.date)
                presenter.loadLessons(model.date)
            }
            R.id.cl_lessons_container -> {
                val lesson = presenter.lessons[position] as LessonViewModel
                createDeleteLessonAlert(" ", lesson)?.show()
            }
            R.id.ll_month_item -> {
                val model = presenter.months[position] as DateViewModel
                if (position == 0) {
                    rv_date_line.layoutManager?.scrollToPosition(position)
                    rv_date_line.afterMeasured {
                        layoutManager?.findViewByPosition(position)!!.callOnClick()
                    }
                }
                else {
                    val firstDayOfMonthPosition = presenter.firstDayOfMonthPosition(model.date)
                    rv_date_line.layoutManager?.scrollToPosition(firstDayOfMonthPosition)
                    rv_date_line.afterMeasured {
                        layoutManager?.findViewByPosition(firstDayOfMonthPosition)!!.callOnClick()
                    }
                }
            }
            R.id.cl_add_lessons_container -> {
                presenter.setPeriod(true)
                (activity as MainActivity).openScreen(AddLessonFragment.TAG, isAddToBackStack = false)
            }

            else -> null
        }
    }

    override fun onDatesLoaded(dates: List<IDateViewModel>, months: List<IDateViewModel>, position: Int?) {
        srl_lessons_updater.isRefreshing = false
        if (presenter.dates.isNotEmpty()) {
            bs_rv_container.visibility = View.VISIBLE
            daysAdapter.update(dates)
            monthsAdapter.update(months)
            rv_date_line.layoutManager?.scrollToPosition(position ?: 0)
            rv_date_line.afterMeasured {
                layoutManager?.findViewByPosition(position ?: 0)!!.callOnClick()
            }
        } else {
            bs_rv_container.visibility = View.GONE
        }
    }

    override fun onGettingEmptyDates() {
        srl_lessons_updater.isRefreshing = false
        pb_lessons_empty.visibility = View.GONE
        tv_lessons_empty.visibility = View.VISIBLE
        rv_lessons_list.visibility = View.GONE
        bs_rv_container.visibility = View.GONE
    }

    override fun onLessonsLoaded(lessons: ArrayList<ILessonViewModel>) {
        presenter.updateLessons(lessons)
        srl_lessons_updater.isRefreshing = false
        pb_lessons_empty.visibility = View.GONE
        if (presenter.dates.isNotEmpty()) {
            tv_lessons_empty.visibility = View.GONE
            rv_lessons_list.visibility = View.VISIBLE
            lessonsAdapter.update(lessons)
        } else {
            tv_lessons_empty.visibility = View.VISIBLE
            rv_lessons_list.visibility = View.GONE
        }
    }

    override fun onLessonDeleted() {
        presenter.loadLessons()
    }

    private inline fun  RecyclerView.afterMeasured(crossinline f: RecyclerView.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
    private fun SnapHelper.scrollToPosition(position: Int) {
        rv_date_line.layoutManager!!.findViewByPosition(position)?.let { rvView ->
            val snapDistance = calculateDistanceToFinalSnap(rv_date_line.layoutManager!!, rvView)
            if (snapDistance?.get(0) != 0 || snapDistance[1] != 0) {
                rv_date_line.scrollBy(snapDistance?.get(0)!!, snapDistance[1])
            }
        }
    }

    private fun SnapHelper.smoothScrollToPosition(position: Int) {
        rv_date_line.layoutManager!!.findViewByPosition(position)?.let { rvView ->
            val snapDistance = calculateDistanceToFinalSnap(rv_date_line.layoutManager!!, rvView)
            if (snapDistance?.get(0) != 0 || snapDistance[1] != 0) {
                rv_date_line.smoothScrollBy(snapDistance?.get(0)!!, snapDistance[1])
            }
        }
    }

    private fun Fragment.createDeleteLessonAlert(message: String, lesson: LessonViewModel): AlertDialog? {
        return AlertDialog.Builder(context!!)
            .setTitle("Удаление пары")
            .setMessage("Вы действительно хотите удалить эту пару? " + message)
            .setPositiveButton("Да") { _, _ ->
                presenter.deleteLesson(lesson)
            }
            .create()
    }

    companion object {
        val TAG = "LessonsSheduleFragment"
        fun newInstance(): LessonsScheduleFragment {
            return LessonsScheduleFragment()
        }
    }
}