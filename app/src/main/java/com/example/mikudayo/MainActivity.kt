package com.example.mikudayo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.mikudayo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null

    // Bouncing logo animation properties
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 16L // ~60 FPS
    private var dx = 5 // Horizontal speed
    private var dy = 5 // Vertical speed

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Permissions are requested as a distraction, so we don't strictly need to check the result
        // We proceed regardless of the outcome.
        startVideoPlayback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the full-screen theme
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        // Request permissions first
        requestAppPermissions()
    }

    private fun requestAppPermissions() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO // Just for distraction
        )

        // File permissions are tricky on modern Android, but we request them for the "prank"
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        permissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    private fun startVideoPlayback() {
        player = ExoPlayer.Builder(this).build().apply {
            binding.videoPlayerView.player = this
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        releasePlayer()
                        showBouncingLogoScreen()
                    }
                }
            })

            // The video is in the 'raw' folder, which the user will provide as 'start.mp4'
            val mediaItem = MediaItem.fromUri(Uri.parse("android.resource://" + packageName + "/" + R.raw.start))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    private fun showBouncingLogoScreen() {
        binding.videoPlayerView.visibility = View.GONE
        binding.bouncingLogoContainer.visibility = View.VISIBLE

        // Load the user's image (assuming it's named 'prank_logo' in drawable)
        // The user will place their image in the drawable folder.
        binding.bouncingImageView.setImageResource(R.drawable.prank_logo)

        // Set initial random position
        binding.bouncingImageView.post {
            val containerWidth = binding.bouncingLogoContainer.width
            val containerHeight = binding.bouncingLogoContainer.height
            val logoWidth = binding.bouncingImageView.width
            val logoHeight = binding.bouncingImageView.height

            // Randomize initial position
            binding.bouncingImageView.x = Random.nextFloat() * (containerWidth - logoWidth)
            binding.bouncingImageView.y = Random.nextFloat() * (containerHeight - logoHeight)

            // Start the animation loop
            handler.post(bouncingRunnable)
        }

        // Set click listener to transition to the next screen
        binding.bouncingImageView.setOnClickListener {
            val intent = Intent(this, AudioSelectActivity::class.java)
            startActivity(intent)
            // Prevent going back to the video screen
            finish()
        }
    }

    private val bouncingRunnable = object : Runnable {
        override fun run() {
            val logo = binding.bouncingImageView
            val container = binding.bouncingLogoContainer

            val containerWidth = container.width
            val containerHeight = container.height
            val logoWidth = logo.width
            val logoHeight = logo.height

            var newX = logo.x + dx
            var newY = logo.y + dy

            // Check for horizontal collision
            if (newX + logoWidth > containerWidth || newX < 0) {
                dx = -dx // Reverse direction
                newX = if (newX < 0) 0f else (containerWidth - logoWidth).toFloat() // Snap to edge
            }

            // Check for vertical collision
            if (newY + logoHeight > containerHeight || newY < 0) {
                dy = -dy // Reverse direction
                newY = if (newY < 0) 0f else (containerHeight - logoHeight).toFloat() // Snap to edge
            }

            logo.x = newX
            logo.y = newY

            handler.postDelayed(this, updateInterval)
        }
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        handler.removeCallbacks(bouncingRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (binding.bouncingLogoContainer.visibility == View.VISIBLE) {
            handler.post(bouncingRunnable)
        } else if (player == null) {
            // Re-initialize player if needed, though the flow should prevent this
            // unless the user leaves and comes back before the video ends.
            // For simplicity, we assume the user stays until the video ends or the app is closed.
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        handler.removeCallbacks(bouncingRunnable)
    }
}
