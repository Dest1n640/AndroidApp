package com.example.a10_project

import android.os.SystemClock
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemorinaTest {
    @Test
    fun cardsOpenAndCloseTwoAtATime() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val cards = cardsOn(activity)
                val closed = activity.getString(R.string.card_closed)
                assertEquals(16, cards.size)
                assertEquals(List(8) { 2 }, cards.groupingBy { it.tag }.eachCount().values.toList())
                assertTrue(cards.all { it.contentDescription == closed })

                val names = cards.map { card ->
                    card.performClick()
                    assertNotNull(card.drawable)
                    val name = card.contentDescription
                    card.performClick()
                    assertEquals(closed, card.contentDescription)
                    name
                }
                assertEquals(8, names.toSet().size)
                assertTrue(closed !in names)

                val (first, second, third) = cards
                first.performClick()
                second.performClick()
                third.performClick()
                assertEquals(2, cards.count { it.contentDescription != closed })
                assertEquals(closed, third.contentDescription)

                first.performClick()
                third.performClick()
                assertEquals(closed, first.contentDescription)
                assertEquals(names[2], third.contentDescription)
            }

            SystemClock.sleep(1_100)
            scenario.onActivity { activity ->
                val closed = activity.getString(R.string.card_closed)
                assertTrue(cardsOn(activity).all { it.contentDescription == closed })
            }
        }
    }

    private fun cardsOn(activity: MainActivity): List<ImageView> =
        activity.findViewById<ViewGroup>(R.id.board).children
            .flatMap { (it as ViewGroup).children }
            .map { it as ImageView }
            .toList()
}
