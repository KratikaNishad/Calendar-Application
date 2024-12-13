package com.example.calendarapplication.network

import com.example.calendarapplication.model.DeleteTaskRequest
import com.example.calendarapplication.model.GetCalendarTaskListResponse
import com.example.calendarapplication.model.TaskFetch
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.model.TaskResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("api/storeCalendarTask")
    suspend fun storeCalendarTask(@Body taskRequest: TaskRequest): Response<TaskResponse>

    @POST("api/getCalendarTaskList")
    suspend fun getTaskList(@Body userRequest: TaskFetch): Response<GetCalendarTaskListResponse>

    @POST("api/deleteCalendarTask")
    suspend fun deleteTask(@Body deleteTaskRequest: DeleteTaskRequest): Response<TaskResponse>

}