package com.unitbean.studentappkotlin.ui.lessonsSchedule.models

import android.os.Parcelable
import com.unitbean.studentappkotlin.utils.adapters.DiffComparable
import kotlinx.android.parcel.Parcelize
import java.util.*

interface IDateViewModel : DiffComparable

@Parcelize
data class DateViewModel(val date: Date): IDateViewModel, Parcelable {
    override fun getItemId(): Int = date.hashCode()
}