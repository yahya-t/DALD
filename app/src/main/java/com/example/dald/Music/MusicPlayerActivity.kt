package com.example.dald.Music

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dald.MainActivity
import com.example.dald.R
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {
    lateinit var musicTitle: TextView
    lateinit var musicCurrentTime: TextView
    lateinit var musicTotalTime: TextView
    lateinit var musicSeekBar: SeekBar
    lateinit var musicPlayerImage: ImageView
    lateinit var musicPause: ImageView
    lateinit var musicPrevious: ImageView
    lateinit var musicNext: ImageView

    lateinit var musicList: ArrayList<MusicModel>
    lateinit var currentMusic: MusicModel
    var mediaPlayer: MediaPlayer? = CustomMediaPlayer.getInstance()
    var x = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        hideSystemUI()
        supportActionBar?.hide()

        musicTitle = findViewById(R.id.music_player_title)
        musicCurrentTime = findViewById(R.id.music_current_time)
        musicTotalTime = findViewById(R.id.music_total_time)
        musicSeekBar = findViewById(R.id.music_seek_bar)
        musicPlayerImage = findViewById(R.id.music_player_image)
        musicPause = findViewById(R.id.music_pause)
        musicPrevious = findViewById(R.id.music_previous)
        musicNext = findViewById(R.id.music_next)

        musicList = intent.getSerializableExtra("LIST") as ArrayList<MusicModel>

        setResourcesWithMusic()

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    musicSeekBar?.progress = mediaPlayer!!.currentPosition
                    musicCurrentTime?.text = convertToMMSS(mediaPlayer!!.currentPosition.toString() + "")
                    if (mediaPlayer!!.isPlaying) {
                        musicPause?.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        musicPlayerImage?.rotation = x++.toFloat()
                    } else {
                        musicPause?.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                        musicPlayerImage?.rotation = 0F
                    }
                }
                Handler().postDelayed(this, 100)
            }
        })

        musicSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }


    private fun setResourcesWithMusic() {
        currentMusic = musicList[CustomMediaPlayer.currentIndex]
        musicTitle!!.text = currentMusic?.title
        musicTotalTime?.text = currentMusic?.duration?.let { convertToMMSS(it) }
        musicPause?.setOnClickListener { pausePlay() }
        musicNext?.setOnClickListener { playNextSong() }
        musicPrevious?.setOnClickListener { playPreviousSong() }
        playMusic()
    }


    private fun playMusic() {
        mediaPlayer!!.reset()
        try {
            mediaPlayer?.setDataSource(currentMusic?.path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            musicSeekBar?.progress = 0
            musicSeekBar?.max = mediaPlayer!!.duration
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (CustomMediaPlayer.currentIndex == musicList!!.size - 1) {
            return
        } else {
            CustomMediaPlayer.currentIndex += 1
            mediaPlayer!!.reset()
            setResourcesWithMusic()
        }
    }

    private fun playPreviousSong() {
        if (CustomMediaPlayer.currentIndex == 0) {
            return
        } else {
            CustomMediaPlayer.currentIndex -= 1
            mediaPlayer!!.reset()
            setResourcesWithMusic()
        }
    }

    private fun pausePlay() {
        if (mediaPlayer!!.isPlaying) mediaPlayer!!.pause() else mediaPlayer!!.start()
    }


    private fun convertToMMSS(duration: String): String? {
        val millis = duration.toLong()
        return java.lang.String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))
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

    fun returnToMusic(view: View) {
        mediaPlayer!!.pause()
        finish()
        val intent = Intent(this, MusicActivity::class.java)
        startActivity(intent)

    }
}