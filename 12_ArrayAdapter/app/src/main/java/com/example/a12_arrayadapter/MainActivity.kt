package com.example.a12_arrayadapter

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import kotlin.math.roundToInt

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

        val names = resources.getStringArray(R.array.first_names)
        val surnames = resources.getStringArray(R.array.last_names)
        fun randomName() = getString(R.string.full_name, names.random(), surnames.random())

        val people = MutableList(5) { randomName() }
        val list = findViewById<ListView>(R.id.people)
        val selectionColor = MaterialColors.getColor(list, com.google.android.material.R.attr.colorSecondaryContainer)
        var selectedPosition = ListView.INVALID_POSITION
        val adapter = object : ArrayAdapter<String>(this, R.layout.person_row, people) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = super.getView(position, convertView, parent) as CheckedTextView
                row.isChecked = position == selectedPosition
                row.setBackgroundColor(if (row.isChecked) selectionColor else Color.TRANSPARENT)
                return row
            }
        }
        list.adapter = adapter
        val scrollFactor = ViewConfiguration.get(this).scaledVerticalScrollFactor
        list.setOnGenericMotionListener { _, event ->
            if (event.action == MotionEvent.ACTION_SCROLL) {
                val delta = event.getAxisValue(MotionEvent.AXIS_VSCROLL)
                    .takeIf { it != 0f } ?: event.getAxisValue(MotionEvent.AXIS_SCROLL)
                if (delta != 0f) {
                    val px = (-delta * scrollFactor).roundToInt().let {
                        if (it != 0) it else if (delta > 0f) -1 else 1
                    }
                    list.scrollListBy(px)
                    return@setOnGenericMotionListener true
                }
            }
            false
        }
        list.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            adapter.notifyDataSetChanged()
        }
        findViewById<Button>(R.id.add_person).setOnClickListener {
            people.add(randomName())
            adapter.notifyDataSetChanged()
            list.setSelection(people.lastIndex)
        }
    }
}
