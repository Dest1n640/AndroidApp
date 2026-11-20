package com.example.a10_project

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemorinaTest {
    @Test
    fun differentCardsCloseTogetherAfterOneSecond() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val cards = cardsOn(activity)
                val closed = activity.getString(R.string.card_closed)
                assertEquals(16, cards.size)
                assertEquals(List(8) { 2 }, cards.groupingBy { it.tag }.eachCount().values.toList())
                assertTrue(cards.all { it.contentDescription == closed })

                val first = cards.first()
                val second = cards.first { it.tag != first.tag }
                val third = cards.first { it !== first && it !== second }
                first.performClick()
                val firstFace = first.contentDescription
                assertNotEquals(closed, firstFace)
                first.performClick()
                assertEquals(firstFace, first.contentDescription)

                second.performClick()
                third.performClick()
                assertEquals(2, cards.count { it.contentDescription != closed })
                assertNotEquals(firstFace, second.contentDescription)
                assertEquals(closed, third.contentDescription)
            }

            SystemClock.sleep(1_100)
            scenario.onActivity { activity ->
                val cards = cardsOn(activity)
                val closed = activity.getString(R.string.card_closed)
                assertTrue(cards.all { it.contentDescription == closed && it.visibility == View.VISIBLE })
                cards.last().performClick()
                assertNotEquals(closed, cards.last().contentDescription)
            }
        }
    }

    @Test
    fun matchingPairsDisappearAndLastPairWins() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val pair = cardsOn(activity).groupBy { it.tag }.values.first()
                pair.forEach { it.performClick() }
                assertTrue(pair.all { it.visibility == View.INVISIBLE })
                assertEquals(View.GONE, activity.findViewById<View>(R.id.win_message).visibility)
            }

            scenario.recreate()
            scenario.onActivity { activity ->
                val cards = cardsOn(activity)
                assertEquals(2, cards.count { it.visibility == View.INVISIBLE })
                val remainingPairs = cards.filter { it.visibility == View.VISIBLE }.groupBy { it.tag }.values
                remainingPairs.forEach { pair -> pair.forEach { it.performClick() } }
                assertTrue(cards.all { it.visibility == View.INVISIBLE })
                assertEquals(View.VISIBLE, activity.findViewById<View>(R.id.win_message).visibility)
            }
        }
    }

    private fun cardsOn(activity: MainActivity): List<ImageView> =
        activity.findViewById<ViewGroup>(R.id.board).children
            .flatMap { (it as ViewGroup).children }
            .map { it as ImageView }
            .toList()
}
