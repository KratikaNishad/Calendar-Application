package com.example.calendarapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapplication.model.DeleteTaskRequest
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.model.TaskResponse
import retrofit2.Response
import com.example.calendarapplication.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun storeTask(taskRequest: TaskRequest, onResult: (Response<TaskResponse>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = taskRepository.storeTask(taskRequest)
                onResult(response)
            } catch (e: Exception) {
                onResult(Response.error(400, null))
            }
        }
    }

    fun deleteTask(userId: Int, taskId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response =
                    taskRepository.deleteTask(DeleteTaskRequest(user_id = userId, task_id = taskId))
                if (response.isSuccessful && response.body()?.status == "Success") {
                    onSuccess()
                } else {
                    onFailure("Failed to delete task")
                }
            } catch (e: Exception) {
                onFailure("Error: ${e.message}")
            }
        }
    }

}