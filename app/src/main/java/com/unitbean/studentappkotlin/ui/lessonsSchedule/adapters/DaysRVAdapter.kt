package com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.lessonsSchedule.listeners.TransitionClickListener
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.DateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.IDateViewModel
import com.unitbean.studentappkotlin.utils.adapters.BaseListAdapter
import kotlinx.android.synthetic.main.rv_date_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class DaysRVAdapter : BaseListAdapter<IDateViewModel, RecyclerView.ViewHolder>() {

    private val dayFormatter: SimpleDateFormat by lazy { SimpleDateFormat("d", Locale.getDefault()) }
    private val dayOfTheWeekFormatter: SimpleDateFormat by lazy { SimpleDateFormat("EEEE", Locale.getDefault()) }
    var transitionClickListener: TransitionClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_date_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DayViewHolder) {
            holder.bind(getItem(position))
        }
    }

    private inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val container: LinearLayout = view.ll_date_item
        private val day: TextView = view.tv_date_day
        private val dayOfTheWeek: TextView = view.tv_date_day_of_the_week

        init {
            container.setOnClickListener(this)
        }

        fun bind(model: IDateViewModel) {
            val dateToken = model as DateViewModel

            day.text = dayFormatter.format(dateToken.date)
            dayOfTheWeek.text = when (dayOfTheWeekFormatter.format(dateToken.date)) {
                "понедельник" -> "ПН"
                "вторник" -> "ВТ"
                "среда" -> "СР"
                "четверг" -> "ЧТ"
                "пятница" -> "ПТ"
                "суббота" -> "СБ"
                "воскресенье" -> "ВС"
                else -> dayOfTheWeekFormatter.format(dateToken.date)
            }


        }

        override fun onClick(v: View) {
            transitionClickListener?.onItemClick(v, adapterPosition, null)
        }
    }
}
