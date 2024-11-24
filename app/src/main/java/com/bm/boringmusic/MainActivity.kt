package com.bm.boringmusic

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bm.boringmusic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO) // 针对 Android 13 及以上版本
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) // 针对 Android 12 及以下版本
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = permissions.filter {
            checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            val allGranted = permissionsResult.values.all { it }
            if (allGranted) {
                recreate() // 权限授予后重新加载
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    private fun playAudio(filePath: String) {
        try {
            Log.d("MainActivity", "Attempting to play audio: $filePath")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
            }
            Log.d("MainActivity", "Audio playback started")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error playing audio: ${e.message}", e)
            Toast.makeText(this, "Unable to play audio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private var globalMediaPlayer: MediaPlayer? = null
        private var currentGlobalAudioPath: String? = null
        private var isGlobalPlaying: Boolean = false

        fun playAudioGlobally(context: Context, filePath: String) {
            try {
                if (currentGlobalAudioPath == filePath) {
                    // 如果两次点击同一首歌，则暂停或继续播放
                    if (isGlobalPlaying) {
                        globalMediaPlayer?.pause()
                        isGlobalPlaying = false
                        Toast.makeText(context, "Paused: $filePath", Toast.LENGTH_SHORT).show()
                    } else {
                        globalMediaPlayer?.start()
                        isGlobalPlaying = true
                        Toast.makeText(context, "Resumed: $filePath", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 播放新歌曲
                    globalMediaPlayer?.release()
                    globalMediaPlayer = MediaPlayer().apply {
                        setDataSource(filePath)
                        prepare()
                        start()
                    }
                    currentGlobalAudioPath = filePath
                    isGlobalPlaying = true
                    Toast.makeText(context, "Playing: $filePath", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Unable to play audio: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}