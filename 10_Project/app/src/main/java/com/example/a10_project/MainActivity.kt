package com.example.a10_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private val handler = Handler(Looper.getMainLooper())
    private val openCards = mutableSetOf<ImageView>()
    private lateinit var deck: IntArray

    private val colorListener = View.OnClickListener { view ->
        val card = view as ImageView
        if (card in openCards) {
            handler.removeCallbacksAndMessages(card)
            closeCard(card)
        } else if (openCards.size < 2) {
            val (image, name) = faces[card.tag as Int]
            openCards.add(card)
            card.setImageResource(image)
            card.contentDescription = getString(name)
            handler.postDelayed({ closeCard(card) }, card, 1_000L)
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
        val board = findViewById<LinearLayout>(R.id.board)
        val spacing = (4 * resources.displayMetrics.density).roundToInt()

        for (rowFaces in deck.toList().chunked(4)) {
            val row = LinearLayout(this)
            val rowParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            board.addView(row, rowParams)
            for (face in rowFaces) {
                val card = ImageView(this).apply {
                    tag = face
                    setImageResource(R.drawable.card_back)
                    contentDescription = getString(R.string.card_closed)
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
    }

    private fun closeCard(card: ImageView) {
        openCards.remove(card)
        card.setImageResource(R.drawable.card_back)
        card.contentDescription = getString(R.string.card_closed)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("deck", deck)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
