package com.example.a08_counter_on_mvvm

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun counterIncrementsAndSurvivesActivityRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            onView(withId(R.id.text_counter)).check(matches(withText("0")))
            repeat(3) {
                onView(withId(R.id.btn_increment)).perform(click())
            }
            onView(withId(R.id.text_counter)).check(matches(withText("3")))

            scenario.recreate()

            onView(withId(R.id.text_counter)).check(matches(withText("3")))
            onView(withId(R.id.btn_increment)).perform(click())
            onView(withId(R.id.text_counter)).check(matches(withText("4")))
        }
    }
}
