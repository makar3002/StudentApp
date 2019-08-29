package com.unitbean.studentappkotlin.ui.lessonsSchedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.lessonsSchedule.listeners.TransitionClickListener
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.DateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.IDateViewModel
import com.unitbean.studentappkotlin.utils.adapters.BaseListAdapter
import kotlinx.android.synthetic.main.rv_month_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthsRVAdapter : BaseListAdapter<IDateViewModel, RecyclerView.ViewHolder>() {
    private val monthFormatter: SimpleDateFormat by lazy { SimpleDateFormat("MMMM", Locale.getDefault()) }
    var transitionClickListener: TransitionClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return R.layout.rv_month_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MonthViewHolder) {
            holder.bind(getItem(position))
        }
    }

    private inner class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val container: LinearLayout = view.ll_month_item
        private val month: TextView = view.tv_month

        init {
            container.setOnClickListener(this)
        }

        fun bind(model: IDateViewModel) {
            val dateToken = model as DateViewModel
            month.text = when (monthFormatter.format(dateToken.date)) {
                "января" -> "Январь"
                "февраля" -> "Февраль"
                "марта" -> "Март"
                "апреля" -> "Апрель"
                "мая" -> "Май"
                "июня" -> "Июнь"
                "июля" -> "Июль"
                "августа" -> "Август"
                "сентября" -> "Сентябрь"
                "октября" -> "Октябрь"
                "ноября" -> "Ноябрь"
                "декабря" -> "Декабрь"
                else -> monthFormatter.format(dateToken.date)
            }


        }

        override fun onClick(v: View) {
            transitionClickListener?.onItemClick(v, adapterPosition, null)
        }
    }
}

