package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
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

    fun onGuessClick(view: View) {
        val etBegin = findViewById<EditText>(R.id.begin)
        val etEnd = findViewById<EditText>(R.id.end)

        val begin = etBegin.text.toString().toIntOrNull()
        val end = etEnd.text.toString().toIntOrNull()
        if (begin == null) etBegin.error = "Введите число"
        if (end == null) etEnd.error = "Введите число"
        if (begin == null || end == null) return
        if (begin >= end) {
            etEnd.error = "Должно быть больше начала"
            return
        }

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("begin", begin)
        intent.putExtra("end", end)
        startActivity(intent)
    }
}
