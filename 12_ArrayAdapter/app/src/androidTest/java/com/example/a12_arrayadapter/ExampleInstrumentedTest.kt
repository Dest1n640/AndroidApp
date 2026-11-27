package com.example.a12_arrayadapter

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.widget.ListView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.Matchers.anything
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.a12_arrayadapter", appContext.packageName)
    }

    @Test
    fun keepsOneSelectionWhileScrollingAndAppendsAPerson() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            fun row(position: Int) = onData(anything()).inAdapterView(withId(R.id.people))
                .atPosition(position)

            onView(withId(R.id.people)).check { view, _ ->
                assertEquals(5, (view as ListView).count)
            }

            row(0).perform(click()).check(matches(isChecked()))
            row(1).perform(click()).check(matches(isChecked()))
            row(0).check(matches(isNotChecked()))

            repeat(15) {
                onView(withId(R.id.add_person)).perform(click())
            }
            onView(withId(R.id.people)).check { view, _ ->
                val list = view as ListView
                assertEquals(20, list.count)
                assertEquals(19, list.lastVisiblePosition)
            }

            row(0).check(matches(isNotChecked()))
            row(1).check(matches(isChecked()))

            scenario.onActivity { activity ->
                val list = activity.findViewById<ListView>(R.id.people)
                val now = SystemClock.uptimeMillis()
                val props = arrayOf(MotionEvent.PointerProperties().apply { id = 0 })
                val coords = arrayOf(MotionEvent.PointerCoords().apply {
                    x = list.width / 2f
                    y = list.height / 2f
                    setAxisValue(MotionEvent.AXIS_SCROLL, -3f)
                })
                val event = MotionEvent.obtain(
                    now, now, MotionEvent.ACTION_SCROLL, 1, props, coords,
                    0, 0, 1f, 1f, 0, 0, InputDevice.SOURCE_MOUSE, 0
                )
                assertTrue(list.dispatchGenericMotionEvent(event))
                event.recycle()
                assertTrue(list.firstVisiblePosition > 0)
            }
        }
    }
}
