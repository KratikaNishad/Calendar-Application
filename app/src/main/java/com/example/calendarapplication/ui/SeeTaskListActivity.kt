package com.example.calendarapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapplication.R
import com.example.calendarapplication.adapter.TaskListAdapter
import com.example.calendarapplication.model.TaskFetch
import com.example.calendarapplication.model.TaskItem
import com.example.calendarapplication.network.RetrofitClient
import com.example.calendarapplication.repository.TaskRepository
import com.example.calendarapplication.utils.InternetConnectivityCheck
import com.example.calendarapplication.viewmodel.TaskViewModel
import com.example.calendarapplication.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

class SeeTaskListActivity : AppCompatActivity() {

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var userIdEditText: AppCompatEditText
    private lateinit var emptyList: AppCompatTextView
    private lateinit var deleteNote: AppCompatTextView
    private lateinit var btnFetchTasks: AppCompatButton
    private lateinit var mBackIcon: AppCompatTextView
    private var userId: String? = null
    private val taskRepository = TaskRepository(RetrofitClient.apiInterface)
    private val taskViewModel: TaskViewModel by viewModels { TaskViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_task_list)

        taskRecyclerView = findViewById(R.id.recyclerViewTaskList)
        progressBar = findViewById(R.id.progressBar)
        userIdEditText = findViewById(R.id.etUserId)
        btnFetchTasks = findViewById(R.id.btnFetchTasks)
        emptyList = findViewById(R.id.emptyList)
        deleteNote = findViewById(R.id.deleteNote)
        mBackIcon = findViewById(R.id.backIcon)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        btnFetchTasks.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            userId = userIdEditText.text.toString().trim()
            if (userId!!.isNotEmpty()) {
                fetchTaskList(userId!!)
            } else {
                deleteNote.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.label_please_enter_a_user_id),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mBackIcon.setOnClickListener {
            finish()
        }
    }

    private fun fetchTaskList(userId: String) {
        if (!InternetConnectivityCheck.isInternetAvailable(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = taskRepository.getTaskList(TaskFetch(user_id = userId))

                progressBar.visibility = View.GONE
                deleteNote.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val taskList = response.body()?.tasks
                    if (!taskList.isNullOrEmpty()) {
                        taskListAdapter = TaskListAdapter(taskList) { task ->
                            deleteTask(task)
                        }
                        taskRecyclerView.adapter = taskListAdapter

                        taskRecyclerView.visibility = View.VISIBLE
                        findViewById<TextView>(R.id.emptyList).visibility = View.GONE
                    } else {
                        taskRecyclerView.visibility = View.GONE
                        findViewById<TextView>(R.id.emptyList).visibility = View.VISIBLE
                    }

                } else {
                    Toast.makeText(
                        this@SeeTaskListActivity,
                        getString(R.string.label_failed_to_load_tasks),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SeeTaskListActivity, "${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteTask(task: TaskItem) {
        if (!InternetConnectivityCheck.isInternetAvailable(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
            return
        }

        taskViewModel.deleteTask(userId!!.toInt(), task.task_id, onSuccess = {
            Toast.makeText(
                this,
                getString(R.string.label_task_deleted_successfully),
                Toast.LENGTH_SHORT
            ).show()
            fetchTaskList(userId!!)
        }, onFailure = { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }
}