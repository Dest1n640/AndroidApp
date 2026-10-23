package com.example.a06_hight_and_width_diff

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.View

const val TAG = "mytag"

class MyView(context: Context) : View(context) {

    // 1. родитель сообщает, сколько места доступно
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: w $measuredWidth, h $measuredHeight")
    }

    // 2. размер установлен (вызывается только при изменении)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged: w $w, h $h (было $oldw x $oldh)")
    }

    // 3. view размещена на экране
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: w ${right - left}, h ${bottom - top}")
    }

    // 4. отрисовка: сравниваем с размерами Canvas
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.YELLOW)
        val d = resources.displayMetrics.density
        Log.d(TAG, "onDraw: canvas w ${canvas.width}, h ${canvas.height}; view w $width, h $height")
        Log.d(TAG, "единицы: px; density $d -> ${width / d} x ${height / d} dp")
    }
}
