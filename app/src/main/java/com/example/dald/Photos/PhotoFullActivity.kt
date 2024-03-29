package com.example.dald.Photos

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.dald.R

/**
 * Class to display image in full screen
 */
class PhotoFullActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_full)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        // get image path from intent
        val imagePath = intent.getStringExtra("path")

        // attach to Glide object
        Glide.with(this).load(imagePath).into(findViewById(R.id.imageView))
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

    fun returnToGallery(view: View) {
        val intent = Intent(this, PhotoMainActivity::class.java)
        startActivity(intent)
    }
}