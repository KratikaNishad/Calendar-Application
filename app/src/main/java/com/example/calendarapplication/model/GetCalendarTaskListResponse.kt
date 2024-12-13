package com.example.calendarapplication.model

data class GetCalendarTaskListResponse(
    val tasks: List<TaskItem>
)

data class TaskDetail(
    val title: String,
    val description: String
)

// represents each individual task
data class TaskItem(
    val task_id: Int,
    val task_detail: TaskDetail
)
