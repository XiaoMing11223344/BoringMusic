package com.bm.boringmusic.data.model

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long
)