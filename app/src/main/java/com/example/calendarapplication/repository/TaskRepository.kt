package com.example.calendarapplication.repository

import com.example.calendarapplication.model.DeleteTaskRequest
import com.example.calendarapplication.model.GetCalendarTaskListResponse
import com.example.calendarapplication.model.TaskFetch
import retrofit2.Response
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.model.TaskResponse
import com.example.calendarapplication.network.ApiService

class TaskRepository(private val apiService: ApiService) {

    suspend fun storeTask(taskRequest: TaskRequest): Response<TaskResponse> {
        return apiService.storeCalendarTask(taskRequest)
    }

    suspend fun getTaskList(userRequest: TaskFetch): Response<GetCalendarTaskListResponse> {
        return apiService.getTaskList(userRequest)
    }

    suspend fun deleteTask(deleteTaskRequest: DeleteTaskRequest): Response<TaskResponse> {
        return apiService.deleteTask(deleteTaskRequest)
    }
}