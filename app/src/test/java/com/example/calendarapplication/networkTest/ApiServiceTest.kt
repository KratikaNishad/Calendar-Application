package com.example.calendarapplication.networkTest

import com.example.calendarapplication.model.Task
import com.example.calendarapplication.model.TaskFetch
import com.example.calendarapplication.model.TaskRequest
import com.example.calendarapplication.network.ApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals


class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test storeCalendarTask success`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"status\":\"Success\"}")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.storeCalendarTask(TaskRequest
            (1, Task("Test", "Test Description")))
        assertEquals(200, response.code())
    }

    @Test
    fun `test getCalendarTaskList success`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"tasks\": [{\"task_id\": 1, \"task_detail\": {\"title\": \"Test\", \"description\": \"Test Task\"}}]}")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getTaskList(TaskFetch("1"))
        assertEquals(200, response.code())
        assertEquals(1, response.body()?.tasks?.size)
    }
}

