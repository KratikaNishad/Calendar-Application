package com.example.calendarapplication.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapplication.R
import java.util.*

class CalendarAdapter(
    private val daysInMonth: List<Any>,
    private val selectedDate: Calendar,
    private val onDateClick: (Date) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_WEEKDAY = 0
    private val VIEW_TYPE_DATE = 1

    class WeekdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weekdayTextView: TextView = itemView.findViewById(R.id.weekdayTextView)
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 7) {
            VIEW_TYPE_WEEKDAY
        } else {
            VIEW_TYPE_DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WEEKDAY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_calendar_weekday, parent, false)
                WeekdayViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_calendar_day, parent, false)
                DateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WeekdayViewHolder) {
            val weekdays = daysInMonth[position] as String
            holder.weekdayTextView.text = weekdays
        } else if (holder is DateViewHolder) {
            val date = daysInMonth[position] as Date
            val calendar = Calendar.getInstance()
            calendar.time = date

            holder.dateTextView.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

            val currentCalendar = Calendar.getInstance()
            val isNextMonth = calendar.get(Calendar.MONTH) != selectedDate.get(Calendar.MONTH) ||
                    calendar.get(Calendar.YEAR) != selectedDate.get(Calendar.YEAR)

            val isCurrentDate =
                calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH) &&
                        calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)

            val isSelectedDate =
                calendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH) &&
                        calendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)

            if (isNextMonth) {
                holder.dateTextView.setTextColor(Color.LTGRAY)
            } else {
                holder.dateTextView.setTextColor(Color.BLACK)
            }

            if (isCurrentDate) {
                holder.dateTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.current_date_color
                    )
                )
                holder.dateTextView.setTextColor(Color.WHITE)
            } else if (isSelectedDate) {
                holder.dateTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.selected_date_color
                    )
                )
                holder.dateTextView.setTextColor(Color.WHITE)
            } else {
                holder.dateTextView.setBackgroundResource(R.drawable.calendar_bg)
            }

            holder.itemView.setOnClickListener {
                onDateClick(date)
            }
        }
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }
}


