package com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.lessonsSchedule.listeners.TransitionClickListener
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.AddLessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.ILessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.LessonViewModel
import com.unitbean.studentappkotlin.utils.adapters.BaseListAdapter
import kotlinx.android.synthetic.main.rv_add_lessons_item.view.*
import kotlinx.android.synthetic.main.rv_lessons_item.view.*

class LessonsRVAdapter : BaseListAdapter<ILessonViewModel, RecyclerView.ViewHolder>() {

    var transitionClickListener: TransitionClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is AddLessonViewModel -> R.layout.rv_add_lessons_item
            else -> R.layout.rv_lessons_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when(viewType) {
            R.layout.rv_add_lessons_item -> AddLessonViewHolder(view)
            R.layout.rv_lessons_item -> LessonViewHolder(view)
            else -> LessonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LessonViewHolder) {
            holder.bind(getItem(position))
        }
    }

    private inner class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val container = view.cl_lessons_container
        private val startTime = view.tv_start_time
        private val endTime = view.tv_end_time
        private val lessonName = view.tv_lesson_name
        private val teacherName = view.tv_teacher_name
        private val auditoryNumber = view.tv_auditory_number


        init {
            container.setOnClickListener(this)
        }

        fun bind(model: ILessonViewModel) {
            val lessonToken = model as LessonViewModel
            startTime.text = lessonToken.startTime
            endTime.text = lessonToken.endTime
            lessonName.text = lessonToken.lessonName
            teacherName.text = lessonToken.teacherName
            auditoryNumber.text = lessonToken.auditoryNumber
        }

        override fun onClick(v: View) {
            transitionClickListener?.onItemClick(v, adapterPosition, null)
        }
    }
    private inner class AddLessonViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val container = view.cl_add_lessons_container

        init {
            container.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            transitionClickListener?.onItemClick(v, adapterPosition, null)
        }
    }
}
