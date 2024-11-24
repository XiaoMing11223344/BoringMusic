package com.bm.boringmusic.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bm.boringmusic.data.model.AudioFile
import com.bm.boringmusic.R

class AudioAdapter(
    private val onItemClick: (AudioFile) -> Unit
) : ListAdapter<AudioFile, AudioAdapter.AudioViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AudioViewHolder(
        itemView: View,
        private val onItemClick: (AudioFile) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textTitle)
        private val artist: TextView = itemView.findViewById(R.id.textArtist)

        fun bind(audioFile: AudioFile) {
            title.text = audioFile.title
            artist.text = audioFile.artist
            itemView.setOnClickListener { onItemClick(audioFile) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AudioFile>() {
        override fun areItemsTheSame(oldItem: AudioFile, newItem: AudioFile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AudioFile, newItem: AudioFile): Boolean {
            return oldItem == newItem
        }
    }
}
