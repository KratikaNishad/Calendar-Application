package com.example.calendarapplication.uiTest

import android.view.WindowManager
import android.widget.TextView
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

// Matcher to find Toasts by their message
fun withToastMessage(message: String): Matcher<Root>? {
    return object : TypeSafeMatcher<Root>() {
        override fun describeTo(description: Description) {
            description.appendText("with toast message: $message")
        }

        override fun matchesSafely(root: Root): Boolean {
            val isToast = root.windowLayoutParams.get().type == WindowManager.LayoutParams.TYPE_TOAST
            val toastMessage = root.decorView.findViewById<TextView>(android.R.id.message)
            return isToast && toastMessage != null && toastMessage.text.contains(message)
        }
    }
}