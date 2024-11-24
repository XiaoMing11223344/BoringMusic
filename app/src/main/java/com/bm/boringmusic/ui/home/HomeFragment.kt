package com.bm.boringmusic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bm.boringmusic.MainActivity
import com.bm.boringmusic.data.model.AudioFile
import com.bm.boringmusic.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AudioAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 初始化 RecyclerView
        adapter = AudioAdapter { audioFile ->
            playAudio(audioFile.path) // 点击播放音频
        }
        binding.recyclerViewAudio.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAudio.adapter = adapter

        // 观察数据
        homeViewModel.audioFiles.observe(viewLifecycleOwner) { audioFiles ->
            adapter.submitList(audioFiles) // 更新列表数据
        }

        return root
    }
    private fun playAudio(filePath: String) {
        MainActivity.playAudioGlobally(requireContext(), filePath)
    }

    private fun displayAudioFiles(audioFiles: List<AudioFile>) {
        binding.textHome.text = audioFiles.joinToString("\n") { it.title }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}