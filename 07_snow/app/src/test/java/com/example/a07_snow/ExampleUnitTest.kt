package com.example.a07_snow

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.sin

class ExampleUnitTest {
    @Test
    fun snowflake_decelerationAndReset() {
        val h = 1000f
        val velocity = 20f

        // Вверху экрана скорость близка к максимальной
        val speedTop = velocity * (1f - 0f / h)
        assertEquals(20f, speedTop, 0.001f)

        // Внизу экрана скорость стремится к нулю
        val speedNearBottom = velocity * (1f - 990f / h)
        assertTrue(speedNearBottom < 0.5f)

        // Проверка сброса наверх
        var y = 990f
        val speed = velocity * (1f - y / h)
        if (speed < 0.5f || y >= h) {
            y = 0f
        } else {
            y += speed
        }
        assertEquals(0f, y, 0.001f)
    }

    @Test
    fun snowflake_horizontalWander() {
        val baseX = 100f
        val y1 = 0f
        val y2 = 45f

        val x1 = baseX + sin((y1 + baseX) / 30f) * 25f
        val x2 = baseX + sin((y2 + baseX) / 30f) * 25f

        // Координата X отклоняется от baseX, но остается в пределах амплитуды ±25
        assertTrue(x1 in (baseX - 25f)..(baseX + 25f))
        assertTrue(x2 in (baseX - 25f)..(baseX + 25f))
        assertNotEquals(x1, x2, 0.001f)
    }
}
