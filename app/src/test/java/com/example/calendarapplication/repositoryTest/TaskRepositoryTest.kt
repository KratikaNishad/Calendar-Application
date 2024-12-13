package com.example.calendarapplication.repositoryTest

import com.example.calendarapplication.model.DeleteTaskRequest
import com.example.calendarapplication.model.GetCalendarTaskListResponse
import com.example.calendarapplication.model.Task
import com.example.calendarapplication.model.TaskDetail
import com.example.calendarapplication.model.TaskFetch
import com.example.calendarapplication.model.TaskItem
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.model.TaskResponse
import com.example.calendarapplication.network.ApiService
import com.example.calendarapplication.repository.TaskRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class TaskRepositoryTest {

    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        taskRepository = TaskRepository(apiService)
    }

    @Test
    fun `test storeTask success`() = runBlocking {
        val taskRequest = TaskRequest(1, Task("Test", "Description"))
        val taskResponse = TaskResponse("Success")
        val response = Response.success(taskResponse)

        Mockito.`when`(apiService.storeCalendarTask(taskRequest)).thenReturn(response)

        val result = taskRepository.storeTask(taskRequest)

        assertEquals(200, result.code())
        assertEquals("Success", result.body()?.status)
    }

    @Test
    fun `test getTaskList success`() = runBlocking {
        val taskFetch = TaskFetch("1")
        val taskList = listOf(TaskItem(1, TaskDetail("Test", "Description")))
        val response = Response.success(GetCalendarTaskListResponse(taskList))

        Mockito.`when`(apiService.getTaskList(taskFetch)).thenReturn(response)

        val result = taskRepository.getTaskList(taskFetch)

        assertEquals(200, result.code())
        assertEquals(1, result.body()?.tasks?.size)
    }

    @Test
    fun `test deleteTask success`() = runBlocking {
        val deleteTaskRequest = DeleteTaskRequest(1, 1)
        val taskResponse = TaskResponse("Success")
        val response = Response.success(taskResponse)

        Mockito.`when`(apiService.deleteTask(deleteTaskRequest)).thenReturn(response)

        val result = taskRepository.deleteTask(deleteTaskRequest)

        assertEquals(200, result.code())
        assertEquals("Success", result.body()?.status)
    }
}
