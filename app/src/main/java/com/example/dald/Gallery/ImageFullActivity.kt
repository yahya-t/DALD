package com.example.dald.Gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.dald.R

class ImageFullActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full)

        val imagePath = intent.getStringExtra("path")
        val imageName = intent.getStringExtra("name")

        supportActionBar?.title = imageName
        Glide.with(this).load(imagePath).into(findViewById(R.id.imageView))
    }
}