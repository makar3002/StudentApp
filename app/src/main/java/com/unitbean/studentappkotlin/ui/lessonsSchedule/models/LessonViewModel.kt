package com.unitbean.studentappkotlin.ui.lessonsSchedule.models

import android.os.Parcelable
import com.unitbean.studentappkotlin.utils.adapters.DiffComparable
import com.unitbean.studentappkotlin.utils.responses.LessonResponse
import kotlinx.android.parcel.Parcelize
import java.util.*

interface ILessonViewModel : DiffComparable

@Parcelize
data class LessonViewModel(val date: Date,
                           val startTime: String,
                           val endTime: String,
                           val lessonName: String,
                           val teacherName: String,
                           val auditoryNumber: String): ILessonViewModel, Parcelable {

    constructor(modelResponse: LessonResponse) : this(
        modelResponse.date,
        modelResponse.startTime,
        modelResponse.endTime,
        modelResponse.lessonName,
        modelResponse.teacherName,
        modelResponse.auditoryNumber
    )

    override fun getItemId(): Int = (date.toString()+startTime+endTime).hashCode()
}

data class AddLessonViewModel(val message: String) : ILessonViewModel {
    override fun getItemId(): Int = message.hashCode()
}