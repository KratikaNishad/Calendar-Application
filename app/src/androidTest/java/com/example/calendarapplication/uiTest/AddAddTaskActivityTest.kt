package com.example.calendarapplication.uiTest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapplication.R
import com.example.calendarapplication.ui.AddTaskActivity
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAddTaskActivityTest {

    @Test
    fun testAddTask_Success() {
        val scenario = ActivityScenario.launch(AddTaskActivity::class.java)

        scenario.onActivity { activity ->
            assertTrue(activity.hasWindowFocus())
        }

        onView(withId(R.id.etTaskTitle)).perform(typeText("New Task"))
        onView(withId(R.id.etTaskDescription)).perform(typeText("This is a new task"))
        onView(withId(R.id.etUserId)).perform(typeText("1"))

        onView(withId(R.id.etUserId)).perform(pressImeActionButton())

        onView(withId(R.id.btnSaveTask)).perform(click())

        Espresso.onIdle()

        onView(withText(R.string.label_task_saved_successfully))
            .inRoot(withToastMessage("Task saved successfully"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAddTask_EmptyFieldsValidation() {
        val scenario = ActivityScenario.launch(AddTaskActivity::class.java)

        scenario.onActivity { activity ->
            assertTrue(activity.hasWindowFocus())
        }

        onView(withId(R.id.etTaskTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.etTaskDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.etUserId)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveTask)).check(matches(isDisplayed()))

        onView(withId(R.id.btnSaveTask)).perform(click())

        Espresso.onIdle()

        onView(withText(R.string.label_please_enter_all_the_fields))
            .inRoot(withToastMessage("Please enter all the fields"))
            .check(matches(isDisplayed()))
    }
}
