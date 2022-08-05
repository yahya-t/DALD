package com.example.dald

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dald.Clock.ClockMainActivity
import com.example.dald.Photos.*
import com.example.dald.Music.MusicMainActivity
import com.example.dald.Notes.NotesMainActivity
import com.example.dald.Phone.PhoneMainActivity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideSystemUI()
        supportActionBar?.hide()
    }

    fun galleryCard(view: View) {
        val intent = Intent(this, PhotoMainActivity::class.java)
        startActivity(intent)
    }

    fun musicPlayerCard(view: View) {
        val intent = Intent(this, MusicMainActivity::class.java)
        startActivity(intent)
    }

    fun notesCard(view: View) {
        val intent = Intent(this, NotesMainActivity::class.java)
        startActivity(intent)
    }

    fun clockCard(view: View) {
        val intent = Intent(this, ClockMainActivity::class.java)
        startActivity(intent)
    }

    fun phoneCard(view: View) {
        val intent = Intent(this, PhoneMainActivity::class.java)
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}