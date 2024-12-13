package com.example.calendarapplication.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapplication.R
import com.example.calendarapplication.adapter.CalendarAdapter
import com.example.calendarapplication.utils.InternetConnectivityCheck
import com.example.calendarapplication.utils.SELECTED_DATE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: AppCompatTextView
    private lateinit var addTask: AppCompatTextView
    private lateinit var seeTask: AppCompatTextView
    private lateinit var tvRecentTask: AppCompatTextView
    private lateinit var tvRecentTaskText: AppCompatTextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var btnPreviousMonth: AppCompatImageView
    private lateinit var btnNextMonth: AppCompatImageView
    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var mBackground: ConstraintLayout
    private var formattedDate: String? = null
    private var mFabOpen: Animation? = null
    private var mFabClose: Animation? = null
    private var mIsTextViewVisible = false

    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        addTask = findViewById(R.id.addTask)
        seeTask = findViewById(R.id.seeTask)
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        btnPreviousMonth = findViewById(R.id.btnPreviousMonth)
        btnNextMonth = findViewById(R.id.btnNextMonth)
        btnAddTask = findViewById(R.id.btnAddTask)
        mBackground = findViewById(R.id.popupBackground)
        tvRecentTask = findViewById(R.id.tvRecentTask)
        tvRecentTaskText = findViewById(R.id.tvRecentTaskText)

        mFabOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_fab_open_anim)
        mFabClose = AnimationUtils.loadAnimation(this, R.anim.rotate_fab_close_anim)
        val spanCount = 7
        calendarRecyclerView.layoutManager = GridLayoutManager(this, spanCount)

        updateCalendar()
        clearRecentTask()
        updateRecentTask()

        btnPreviousMonth.setOnClickListener {
            selectedDate.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        btnNextMonth.setOnClickListener {
            selectedDate.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        btnAddTask.setOnClickListener {
            toggleTextViewVisibility()
        }

        if (!InternetConnectivityCheck.isInternetAvailable(this)) {
            Toast.makeText(
                this, getString(R.string.label_no_internet_connection),
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }

    private fun toggleTextViewVisibility() {
        if (mIsTextViewVisible) {
            showViews()
        } else {
            hideViews()

            addTask.setOnClickListener {
                if (formattedDate == null) {
                    val currentDate = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    formattedDate = dateFormat.format(currentDate.time)
                }

                val intent = Intent(this, AddTaskActivity::class.java)
                intent.putExtra(SELECTED_DATE, formattedDate)
                startActivity(intent)
                showViews()
            }
            seeTask.setOnClickListener {
                val intent = Intent(this, SeeTaskListActivity::class.java)
                startActivity(intent)
                showViews()
            }
        }
    }

    private fun hideViews() {
        btnAddTask.startAnimation(mFabOpen)
        mBackground.setVisibility(View.VISIBLE)
        btnAddTask.setImageDrawable(
            AppCompatResources
                .getDrawable(this, R.drawable.close_popup)
        )
        mIsTextViewVisible = true
    }

    private fun showViews() {
        if (mIsTextViewVisible) {
            btnAddTask.startAnimation(mFabClose)
        }
        if (mBackground.getVisibility() == View.VISIBLE) {
            mBackground.setVisibility(View.GONE)
        }
        btnAddTask.setImageDrawable(
            AppCompatResources
                .getDrawable(this, R.drawable.plus_icon)
        )
        mIsTextViewVisible = false
    }

    private fun updateCalendar() {
        val daysInMonth = mutableListOf<Any>()
        val calendar = selectedDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val weekdays = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        for (day in weekdays) {
            daysInMonth.add(day)
        }

        val monthStartDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
        calendar.add(Calendar.DAY_OF_MONTH, -monthStartDay)

        while (daysInMonth.size < 42 + 7) {
            daysInMonth.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = dateFormat.format(selectedDate.time)

        val adapter = CalendarAdapter(daysInMonth, selectedDate) { date ->
            selectedDate.time = date
            updateCalendar()

            formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        }
        calendarRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateRecentTask()
    }

    private fun clearRecentTask() {
        val sharedPreferences = getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
        if (sharedPreferences == null) {
            tvRecentTask.visibility = View.GONE
            tvRecentTaskText.visibility = View.GONE
        }
        val editor = sharedPreferences.edit()
        editor.remove("recent_task_title")
        editor.remove("recent_task_description")
        editor.remove("recent_task_user_id")
        editor.apply()
    }

    private fun updateRecentTask() {
        val sharedPreferences = getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
        val recentTaskUserId = sharedPreferences.getString("recent_task_user_id", "")
        val recentTaskTitle = sharedPreferences.getString("recent_task_title", "")
        val recentTaskDescription = sharedPreferences.getString("recent_task_description", "")

        if (!recentTaskUserId.isNullOrEmpty() || !recentTaskTitle.isNullOrEmpty() ||
            !recentTaskDescription.isNullOrEmpty()
        ) {
            tvRecentTask.visibility = View.VISIBLE
            tvRecentTaskText.visibility = View.VISIBLE
        }
        tvRecentTask.text =
            formatRecentTaskText(recentTaskUserId, recentTaskTitle, recentTaskDescription)
    }

    private fun formatRecentTaskText(
        userId: String?,
        title: String?,
        description: String?
    ): SpannableString {
        val formattedText = "User ID:  $userId\nTitle:  $title\nDescription:  $description"
        val spannableString = SpannableString(formattedText)

        val userIdLabel = "User ID:"
        val userIdStart = formattedText.indexOf(userIdLabel)
        val userIdEnd = userIdStart + userIdLabel.length
        spannableString.setSpan(
            StyleSpan(Typeface.NORMAL),
            userIdStart,
            userIdEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF000000")),
            userIdStart,
            userIdEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val titleLabel = "Title:"
        val titleStart = formattedText.indexOf(titleLabel)
        val titleEnd = titleStart + titleLabel.length
        spannableString.setSpan(
            StyleSpan(Typeface.NORMAL),
            titleStart,
            titleEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF000000")),
            titleStart,
            titleEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val descriptionLabel = "Description:"
        val descriptionStart = formattedText.indexOf(descriptionLabel)
        val descriptionEnd = descriptionStart + descriptionLabel.length
        spannableString.setSpan(
            StyleSpan(Typeface.NORMAL),
            descriptionStart,
            descriptionEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF000000")),
            descriptionStart,
            descriptionEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

}
