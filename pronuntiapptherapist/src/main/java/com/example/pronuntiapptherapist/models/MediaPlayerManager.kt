package com.example.pronuntiapptherapist.models

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class MediaPlayerManager(context: Context) {
    val context = context
    var mediaPlayer: MediaPlayer? = null

    fun playAudio(audioPath: String) {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
        }
        mediaPlayer = MediaPlayer.create(context, Uri.parse(audioPath))
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
        }
        mediaPlayer!!.setOnCompletionListener {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    fun stopPlayingAudio() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
        }
    }

    fun releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}