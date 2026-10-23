package com.example.randomfilm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // перемешанный список ещё не показанных фильмов
    val films = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onResetClick(null)
    }

    fun onNextClick(view: View) {
        findViewById<TextView>(R.id.film).text = films.removeLastOrNull() ?: getString(R.string.no_more)
    }

    fun onResetClick(view: View?) {
        films.clear()
        films.addAll(resources.getStringArray(R.array.films))
        films.shuffle()
        findViewById<TextView>(R.id.film).setText(R.string.hint)
    }
}
