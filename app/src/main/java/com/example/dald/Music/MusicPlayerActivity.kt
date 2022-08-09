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
import com.example.dald.R
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {

    lateinit var tvMusicPlayerSongTitle: TextView
    lateinit var tvMusicCurrentTime: TextView
    lateinit var tvMusicTotalTime: TextView
    lateinit var sbMusicPlayerSeekBar: SeekBar
    lateinit var ivMusicPlayerImage: ImageView
    lateinit var ivPauseMusic: ImageView
    lateinit var ivPreviousMusic: ImageView
    lateinit var ivNextMusic: ImageView

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

        tvMusicPlayerSongTitle = findViewById(R.id.tv_MusicPlayerSongTitle)
        tvMusicCurrentTime = findViewById(R.id.tv_MusicCurrentTime)
        tvMusicTotalTime = findViewById(R.id.tv_MusicTotalTime)
        sbMusicPlayerSeekBar = findViewById(R.id.sb_MusicPlayerSeekBar)
        ivMusicPlayerImage = findViewById(R.id.iv_MusicPlayerImage)
        ivPauseMusic = findViewById(R.id.iv_PauseMusic)
        ivPreviousMusic = findViewById(R.id.iv_PreviousMusic)
        ivNextMusic = findViewById(R.id.iv_NextMusic)

        musicList = intent.getSerializableExtra("LIST") as ArrayList<MusicModel>

        setResourcesWithMusic()

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    sbMusicPlayerSeekBar?.progress = mediaPlayer!!.currentPosition
                    tvMusicCurrentTime?.text = convertToMMSS(mediaPlayer!!.currentPosition.toString() + "")
                    if (mediaPlayer!!.isPlaying) {
                        ivPauseMusic?.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        ivMusicPlayerImage?.rotation = x++.toFloat()
                    } else {
                        ivPauseMusic?.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                        ivMusicPlayerImage?.rotation = 0F
                    }
                }
                Handler().postDelayed(this, 100)
            }
        })

        sbMusicPlayerSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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
        tvMusicPlayerSongTitle!!.text = currentMusic?.title
        tvMusicTotalTime?.text = currentMusic?.duration?.let { convertToMMSS(it) }
        ivPauseMusic?.setOnClickListener { pausePlay() }
        ivNextMusic?.setOnClickListener { playNextSong() }
        ivPreviousMusic?.setOnClickListener { playPreviousSong() }
        playMusic()
    }


    private fun playMusic() {
        mediaPlayer!!.reset()
        try {
            mediaPlayer?.setDataSource(currentMusic?.path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            sbMusicPlayerSeekBar?.progress = 0
            sbMusicPlayerSeekBar?.max = mediaPlayer!!.duration
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
        val intent = Intent(this, MusicMainActivity::class.java)
        startActivity(intent)

    }
}