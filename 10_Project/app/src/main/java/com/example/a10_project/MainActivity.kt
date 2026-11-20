package com.example.a10_project

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val faces = listOf(
        R.drawable.card_circle to R.string.card_circle,
        R.drawable.card_triangle to R.string.card_triangle,
        R.drawable.card_square to R.string.card_square,
        R.drawable.card_diamond to R.string.card_diamond,
        R.drawable.card_star to R.string.card_star,
        R.drawable.card_moon to R.string.card_moon,
        R.drawable.card_heart to R.string.card_heart,
        R.drawable.card_cross to R.string.card_cross
    )
    private val matchedFaces = mutableSetOf<Int>()
    private var firstCard: ImageView? = null
    private var resolvingPair = false
    private lateinit var deck: IntArray

    private val colorListener = View.OnClickListener { view ->
        val card = view as ImageView
        val face = card.tag as Int
        if (resolvingPair || card === firstCard || face in matchedFaces) return@OnClickListener

        val (image, name) = faces[face]
        card.setImageResource(image)
        card.contentDescription = getString(name)

        val first = firstCard
        if (first == null) {
            firstCard = card
        } else if (first.tag == face) {
            matchedFaces.add(face)
            first.visibility = View.INVISIBLE
            card.visibility = View.INVISIBLE
            firstCard = null
            showWinIfComplete()
        } else {
            resolvingPair = true
            lifecycleScope.launch {
                delay(1_000L)
                closeCard(first)
                closeCard(card)
                firstCard = null
                resolvingPair = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        deck = savedInstanceState?.getIntArray("deck")
            ?: (faces.indices + faces.indices).shuffled().toIntArray()
        savedInstanceState?.getIntArray("matchedFaces")?.let { matchedFaces.addAll(it.asList()) }
        val board = findViewById<LinearLayout>(R.id.board)
        val spacing = (4 * resources.displayMetrics.density).roundToInt()

        for (rowFaces in deck.toList().chunked(4)) {
            val row = LinearLayout(this)
            val rowParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            board.addView(row, rowParams)
            for (face in rowFaces) {
                val card = ImageView(this).apply {
                    tag = face
                    closeCard(this)
                    if (face in matchedFaces) visibility = View.INVISIBLE
                    isFocusable = true
                    setOnClickListener(colorListener)
                }
                val params = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f
                ).apply {
                    setMargins(spacing, spacing, spacing, spacing)
                }
                row.addView(card, params)
            }
        }
        showWinIfComplete()
    }

    private fun closeCard(card: ImageView) {
        card.setImageResource(R.drawable.card_back)
        card.contentDescription = getString(R.string.card_closed)
    }

    private fun showWinIfComplete() {
        if (matchedFaces.size == faces.size) {
            findViewById<View>(R.id.win_message).visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("deck", deck)
        outState.putIntArray("matchedFaces", matchedFaces.toIntArray())
    }
}
