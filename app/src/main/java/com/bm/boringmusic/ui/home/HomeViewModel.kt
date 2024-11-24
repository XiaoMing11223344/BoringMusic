package com.bm.boringmusic.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bm.boringmusic.data.model.AudioFile
import com.bm.boringmusic.data.model.AudioScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _audioFiles = MutableLiveData<List<AudioFile>>()
    val audioFiles: LiveData<List<AudioFile>> = _audioFiles

    init {
        loadAudioFiles()
    }

//    private fun loadAudioFiles() {
//        val context = getApplication<Application>().applicationContext
//        _audioFiles.value = AudioScanner.scanLocalAudioFiles(context)
//    }

    private fun loadAudioFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val audioFiles = AudioScanner.scanLocalAudioFiles(getApplication<Application>().applicationContext)
            Log.d("111111111111", audioFiles.size.toString())
            withContext(Dispatchers.Main) {
                _audioFiles.value = audioFiles
            }
        }
    }
}