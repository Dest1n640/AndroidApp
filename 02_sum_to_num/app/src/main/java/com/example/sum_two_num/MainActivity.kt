package com.example.sum_two_num

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    // TODO: 1) реализовать проверку на пустые значения в полях numA, numB
    // TODO: 2) исправить разметку, чтобы можно было вводить только числа
    // TODO: 3) сделать поддержку вещественных (не целых чисел)

    fun onClick(v: View) {
        val etA = findViewById<EditText>(R.id.numA)
        val etB = findViewById<EditText>(R.id.numB)
        val tvSum = findViewById<TextView>(R.id.sum)

        val strA = etA.text.toString()
        val strB = etB.text.toString()

        val a = strA.toDoubleOrNull()
        val b = strB.toDoubleOrNull()
        if (a == null) etA.error = "Введите число"
        if (b == null) etB.error = "Введите число"
        if (a == null || b == null) return

        val sum = a + b
        val strSum = sum.toString()
        tvSum.text = strSum
    }
}
