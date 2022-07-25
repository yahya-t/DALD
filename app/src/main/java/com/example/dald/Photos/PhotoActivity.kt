package com.example.dald.Photos

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.MainActivity
import com.example.dald.R

class PhotoActivity : AppCompatActivity() {

    // references the "recycler_image" View object
    private var recyclerImage: RecyclerView? = null
    // references the "progress_recycler" View object
    private var progressBar: ProgressBar? = null
    // ArrayList to store all image paths
    private var allPictures: ArrayList<ImagePath>? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        hideSystemUI()
        supportActionBar?.hide()

        // initialise fields
        recyclerImage = findViewById(R.id.recycler_image)
        progressBar = findViewById(R.id.progress_recycler)

        // set properties for recycler_image View
        recyclerImage?.layoutManager = GridLayoutManager(this, 3)
        recyclerImage?.setHasFixedSize(true)

        //Storage Permissions
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        }

        //initialise field
        allPictures = ArrayList()

        // conditions if allPictures is not empty
        if (allPictures!!.isEmpty()) {
            progressBar?.visibility = View.VISIBLE
            allPictures = getAllImages()
            recyclerImage?.adapter = ImageAdapter(this, allPictures!!)
            progressBar?.visibility = View.GONE
        }
    }

    private fun getAllImages(): ArrayList<ImagePath>? {
        // ArrayList to store image paths
        val images = ArrayList<ImagePath>()

        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)

        // *** store all images from the gallery in a Cursor object ***
        val cursor = this.contentResolver.query(allImageUri, projection, null, null, null)

        // do-while loop to add each cursor to the "image" ArrayList
        try {
            cursor!!.moveToFirst()
            do {
                val image = ImagePath()
                image.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // return "images" ArrayList
        return images
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

    fun returnHomeFromGallery(view: View) {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}