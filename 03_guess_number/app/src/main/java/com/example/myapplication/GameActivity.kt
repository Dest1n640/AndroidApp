package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    var begin: Int = 0
    var end: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        begin = intent.getIntExtra("begin", 0)
        end = intent.getIntExtra("end", 100)
        ask()
    }

    fun ask() {
        val tvQuestion = findViewById<TextView>(R.id.question)
        if (begin == end) {
            tvQuestion.text = "Ваше число $begin!"
            findViewById<View>(R.id.yes).visibility = View.GONE
            findViewById<View>(R.id.no).visibility = View.GONE
        } else if (end - begin == 1) {
            // осталось два соседних числа
            tvQuestion.text = "Ваше число $begin?"
        } else {
            tvQuestion.text = "Ваше число больше ${(begin + end) / 2}?"
        }
    }

    fun onYesNoClick(view: View) {
        val mid = (begin + end) / 2
        val twoLeft = end - begin == 1
        when (view.id) {
            R.id.yes -> if (twoLeft) end = begin else begin = mid + 1
            R.id.no -> if (twoLeft) begin = end else end = mid
        }
        ask()
    }
}
