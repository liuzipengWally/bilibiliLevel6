package com.bilibililevel6.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import com.bilibililevel6.BaseFragment
import com.bilibililevel6.Config
import com.bilibililevel6.databinding.FragmentPlayerBinding
import com.bilibililevel6.extensions.dataStore
import com.bilibililevel6.extensions.launchObserve
import com.bilibililevel6.play.intent.PlayerIntent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * author：liuzipeng
 * time: 2022/11/26 23:59
 */
class PlayerFragment : BaseFragment() {
    private var viewBinding: FragmentPlayerBinding? = null
    private val avid by lazy { arguments?.getInt(AVID) ?: 0 }
    private val cid by lazy { arguments?.getInt(CID) ?: 0 }
    private val bvid by lazy { arguments?.getString(BVID) }
    private val player by lazy { ExoPlayer.Builder(requireActivity()).build() }
    private val viewModel by lazy { ViewModelProvider(requireActivity())[PlayerViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.playerView?.player = player
        initListener()
        initObserver()
    }

    private fun initListener() {

    }

    private fun initObserver() {

        lifecycleScope.launch {
            viewModel.send(
                PlayerIntent.FetchVideoStreamInfo(
                    avid,
                    cid
                )
            )
        }

        launchObserve {
            viewModel.playerUiState.filter {
                it.supportFormats.isNotEmpty()
                        && it.videoSources.isNotEmpty()
                        && it.audioSources.isNotEmpty()
            }.collect { data ->
                val checkedQuality =
                    if (requireActivity().dataStore.data.first().isLogged) data.supportFormats.first().quality else Config.maxQualityOnNotLogin
                val videoUrl = data.videoSources.first { it.id == checkedQuality }.baseUrl
                val audioUrl = data.audioSources.first().baseUrl
                play(videoUrl, audioUrl)
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun play(videoUrl: String, audioUrl: String) {
        val dataSourceFactory = DataSource.Factory {
            val dataSource: HttpDataSource =
                DefaultHttpDataSource.Factory().createDataSource()
            // Set a custom authentication request header.
            dataSource.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 BiliDroid/7.6.0 (bbcallen@gmail.com)"
            )
            dataSource.setRequestProperty("referer", "https://www.bilibili.com")
            dataSource
        }
        val videoMediaSource: MediaSource = DefaultMediaSourceFactory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))
        val audioMediaSource: MediaSource = DefaultMediaSourceFactory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(audioUrl))
        val mediaSource = MergingMediaSource(videoMediaSource, audioMediaSource)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()
    }

    fun seekForward() {
        player.seekForward()
    }

    fun seekBack() {
        player.seekBack()
    }

    fun playOrPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    companion object {
        const val AVID = "avid"
        const val CID = "cid"
        const val BVID = "bvid"
        fun newInstance(aid: Int, cid: Int, bvid: String): PlayerFragment {
            return PlayerFragment().apply {
                arguments = Bundle().apply {
                    putInt(AVID, aid)
                    putInt(CID, cid)
                    putString(BVID, bvid)
                }
            }
        }
    }
}