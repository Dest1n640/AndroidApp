package com.example.adaptation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    // порядок совпадает с R.array.pictures
    val pictures = intArrayOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)
    lateinit var prefs: SharedPreferences
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prefs = getPreferences(MODE_PRIVATE)
        spinner = findViewById(R.id.pictures_list)
        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)
        // восстанавливаем сохранённую картинку; onItemSelected сам её покажет
        spinner.setSelection(prefs.getInt("picture", 0))
        spinner.onItemSelectedListener = this
    }

    // кнопка просто переключает список на следующий пункт: car1 -> car2 -> car3 -> car1
    fun onChangePictureClick(v: View) {
        spinner.setSelection((spinner.selectedItemPosition + 1) % pictures.size)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        findViewById<ImageView>(R.id.picture).setImageResource(pictures[position])
        prefs.edit { putInt("picture", position) }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
