package com.example.calendarapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.calendarapplication.R
import com.example.calendarapplication.model.Task
import com.example.calendarapplication.model.TaskFetch
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.utils.InternetConnectivityCheck
import com.example.calendarapplication.utils.SELECTED_DATE
import com.example.calendarapplication.viewmodel.TaskViewModel
import com.example.calendarapplication.viewmodel.TaskViewModelFactory

class AddTaskActivity : AppCompatActivity() {

    private lateinit var tvSelectedDate: AppCompatTextView
    private lateinit var etTaskTitle: AppCompatEditText
    private lateinit var etTaskDescription: AppCompatEditText
    private lateinit var etUserID: AppCompatEditText
    private lateinit var btnSaveTask: AppCompatButton
    private lateinit var taskFetch: TaskFetch
    private lateinit var mBackIcon: AppCompatTextView

    private val taskViewModel: TaskViewModel by viewModels { TaskViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        etUserID = findViewById(R.id.etUserId)
        btnSaveTask = findViewById(R.id.btnSaveTask)
        mBackIcon = findViewById(R.id.backIcon)

        tvSelectedDate.text =
            getString(R.string.label_selected_date, intent.getStringExtra(SELECTED_DATE))

        btnSaveTask.setOnClickListener {
            if (!InternetConnectivityCheck.isInternetAvailable(this)) {
                Toast.makeText(this, R.string.label_no_internet_connection, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            val title = etTaskTitle.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val userID = etUserID.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || userID.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.label_please_enter_all_the_fields), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val task = Task(title = title, description = description)
            val taskRequest = TaskRequest(user_id = userID.toInt(), task = task)

            taskFetch = TaskFetch(userID)
            taskViewModel.storeTask(taskRequest) { response ->
                if (response.isSuccessful) {
                    if (response.code() == 200) {

                        // Save the recent task in SharedPreferences
                        val sharedPreferences =
                            getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("recent_task_user_id", userID)
                        editor.putString("recent_task_title", title)
                        editor.putString("recent_task_description", description)
                        editor.apply()

                        Toast.makeText(
                            this@AddTaskActivity,
                            getString(R.string.label_task_saved_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(
                        this@AddTaskActivity,
                        getString(R.string.label_failed_to_save_task), Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddTaskActivity,
                        getString(
                            R.string.label_failed_to_save_task_please_try_again,
                            response.errorBody()?.string()
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        mBackIcon.setOnClickListener {
            finish()
        }
    }
}