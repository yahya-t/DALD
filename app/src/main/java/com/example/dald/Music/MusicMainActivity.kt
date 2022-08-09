package com.example.dald.Music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.MainActivity
import com.example.dald.R
import java.io.File

class MusicMainActivity : AppCompatActivity() {

    var rvMusicList: RecyclerView? = null
    var tcNoMusicText: TextView? = null
    var musicList: ArrayList<MusicModel> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_main)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        // initiate views
        rvMusicList = findViewById(R.id.rv_MusicList)
        tcNoMusicText = findViewById(R.id.tc_NoMusicText)

        // check permissions
        if (!checkPermission()) {
            requestPermission()
            return
        }

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null)

        while (cursor?.moveToNext() == true) {
            val songData = MusicModel(cursor.getString(1), cursor.getString(0), cursor.getString(2))
            if (File(songData.path).exists()) musicList.add(songData)
        }
        if (musicList.size === 0) {
            tcNoMusicText?.visibility = View.VISIBLE
        } else {
            //recyclerview
            rvMusicList?.layoutManager = LinearLayoutManager(this)
            rvMusicList?.adapter = MusicListAdapter(musicList, applicationContext)
        }
    }

    /**
     * Checks external storage permission for music player
     */
    fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests external storafe permission for music player
     */
    fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ) {
            Toast.makeText(this,
                "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTINGS",
                Toast.LENGTH_SHORT).show()
        } else ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            123)
    }

    /**
     * Defines function when music is resumed
     */
    override fun onResume() {
        super.onResume()
        if (rvMusicList != null) {
            rvMusicList!!.adapter = MusicListAdapter(musicList, applicationContext)
        }
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

    fun returnHomeFromMusic(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}