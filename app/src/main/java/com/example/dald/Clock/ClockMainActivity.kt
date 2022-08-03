package com.example.dald.Clock

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dald.MainActivity
import com.example.dald.R
import java.text.SimpleDateFormat
import java.util.*

lateinit var tvDate: TextView
lateinit var dateTime: String
lateinit var calendar: Calendar
lateinit var simpleDateFormat: SimpleDateFormat

class ClockMainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_main)

        hideSystemUI()
        supportActionBar?.hide()

        tvDate = findViewById(R.id.tv_Date)

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("\t\t\t\tEEEE\nd LLLL yyyy")
        dateTime = simpleDateFormat.format(calendar.time).toString()
        tvDate.text = dateTime

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun returnHomeFromClock(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}