package com.example.dald.Music

import android.media.MediaPlayer

object CustomMediaPlayer {
    var instance: MediaPlayer? = null

    @JvmName("getInstance1")
    fun getInstance(): MediaPlayer? {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance
    }

    var currentIndex = 0
}