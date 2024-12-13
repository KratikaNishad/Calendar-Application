package com.example.calendarapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calendarapplication.repository.TaskRepository
import com.example.calendarapplication.network.RetrofitClient

class TaskViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val apiService = RetrofitClient.apiInterface
        val taskRepository = TaskRepository(apiService)
        return TaskViewModel(taskRepository) as T
    }
}
