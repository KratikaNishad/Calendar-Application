package com.example.calendarapplication.uiTest

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapplication.R
import com.example.calendarapplication.ui.SeeTaskListActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SeeTaskListActivityTest {

    @Test
    fun testFetchTasks() {
        val scenario = ActivityScenario.launch(SeeTaskListActivity::class.java)

        onView(withId(R.id.etUserId)).perform(typeText("1"))
        onView(withId(R.id.btnFetchTasks)).perform(click())

        onView(withId(R.id.recyclerViewTaskList)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyList)).check(matches(not(isDisplayed())))

        scenario.close()
    }

    @Test
    fun testLongPressToShowDeleteAndDeleteTask() {
        val scenario = ActivityScenario.launch(SeeTaskListActivity::class.java)

        onView(withId(R.id.etUserId)).perform(typeText("123"))
        onView(withId(R.id.btnFetchTasks)).perform(click())

        onView(withId(R.id.etUserId)).perform(pressImeActionButton())

        onView(withId(R.id.recyclerViewTaskList)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyList)).check(matches(not(isDisplayed())))

        onView(withId(R.id.recyclerViewTaskList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    longClick()
                )
            )

        onView(
            allOf(
                withId(R.id.deleteTask),
                withText("Delete"),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.deleteTask),
                withText("Delete"),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        ).perform(click())

        onView(withText(R.string.label_task_deleted_successfully))
            .inRoot(withToastMessage("Task deleted successfully"))
            .check(matches(isDisplayed()))

        Thread.sleep(2000)

        // Verifying the empty list message
        onView(withId(R.id.emptyList)).check(matches(isDisplayed()))

        scenario.close()
    }
}
