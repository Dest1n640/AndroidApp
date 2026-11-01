@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package com.example.a07_snow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.View
import kotlin.math.sin
import kotlin.random.Random

data class Snowflake(
    var x: Float,
    var y: Float,
    val velocity: Float,
    val radius: Float,
    val color: Int,
    val baseX: Float = x
)

open class Snowflakes(ctx: Context) : View(ctx) {
    private lateinit var snow: Array<Snowflake>
    private val paint = Paint()
    private var h = 1000
    private var w = 1000
    private var moveTask: MoveTask? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLUE)

        if (!::snow.isInitialized) return
        for (s in snow) {
            paint.color = s.color
            canvas.drawCircle(s.x, s.y, s.radius, paint)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = bottom - top
        w = right - left

        val r = Random
        snow = Array(40) {
            val baseX = r.nextFloat() * w
            val shade = r.nextInt(180, 256)
            Snowflake(
                x = baseX,
                y = r.nextFloat() * h,
                velocity = 8f + 12f * r.nextFloat(),
                radius = 15f + 15f * r.nextFloat(),
                color = Color.rgb(shade, shade, 255),
                baseX = baseX
            )
        }
    }

    fun moveSnowflakes() {
        if (!::snow.isInitialized) return
        for (s in snow) {
            // Замедление к нижней границе экрана (вверху быстрее, внизу медленнее)
            val speed = s.velocity * (1f - s.y / h)
            if (speed < 0.5f || s.y >= h) {
                s.y = 0f
            } else {
                s.y += speed
            }
            // Блуждания по горизонтали с помощью функции синуса
            s.x = s.baseX + sin((s.y + s.baseX) / 30f) * 25f
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (moveTask == null) {
            moveTask = MoveTask(this)
            moveTask?.execute(30)
        }
        return false // защита от множественных срабатываний
    }

    // shortcut: using deprecated AsyncTask as required by assignment, upgrade to coroutines/Flow in modern Android
    class MoveTask(private val s: Snowflakes) : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg params: Int?): Int {
            val delay = (params.getOrNull(0) ?: 30).toLong()
            try {
                while (!isCancelled) {
                    Thread.sleep(delay)
                    publishProgress()
                }
            } catch (_: InterruptedException) {
            }
            return 0
        }

        override fun onProgressUpdate(vararg values: Int?) {
            s.moveSnowflakes()
        }
    }
}
