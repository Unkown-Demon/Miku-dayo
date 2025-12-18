package com.example.mikudayo

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mikudayo.databinding.ActivityAudioSelectBinding

class AudioSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioSelectBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private val volumeHandler = Handler(Looper.getMainLooper())
    private val autoCloseHandler = Handler(Looper.getMainLooper())

    // Runnable to constantly set the volume to max
    private val volumeControlRunnable = object : Runnable {
        override fun run() {
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
            volumeHandler.postDelayed(this, 50) // Check and set volume every 50ms
        }
    }

    // Runnable to close the app after 5 minutes
    private val autoCloseRunnable = Runnable {
        finishAffinity() // Close all activities and exit the app
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the full-screen theme
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Start the auto-close timer (5 minutes = 300,000 milliseconds)
        autoCloseHandler.postDelayed(autoCloseRunnable, 5 * 60 * 1000L)
    }

    fun onRedButtonClick(view: View) {
        startPrankAudio(R.raw.audio1)
    }

    fun onBlueButtonClick(view: View) {
        startPrankAudio(R.raw.audio2)
    }

    private fun startPrankAudio(audioResId: Int) {
        // Stop any currently playing audio
        mediaPlayer?.stop()
        mediaPlayer?.release()

        // Start new audio
        mediaPlayer = MediaPlayer.create(this, audioResId).apply {
            isLooping = true // Loop the audio
            start()
        }

        // Start the volume control loop
        volumeHandler.removeCallbacks(volumeControlRunnable)
        volumeHandler.post(volumeControlRunnable)

        // Hide the buttons after selection
        binding.redButton.visibility = View.GONE
        binding.blueButton.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
        volumeHandler.removeCallbacks(volumeControlRunnable)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            volumeHandler.post(volumeControlRunnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        volumeHandler.removeCallbacks(volumeControlRunnable)
        autoCloseHandler.removeCallbacks(autoCloseRunnable)
    }
}
