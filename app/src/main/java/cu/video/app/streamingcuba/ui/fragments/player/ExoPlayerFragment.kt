package cu.video.app.streamingcuba.ui.fragments.player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.Parameters
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.databinding.FragmentExoPlayerBinding
import cu.video.app.streamingcuba.databinding.FragmentFullPlayerBinding
import cu.video.app.streamingcuba.streaming.track.PlayerErrorMessageProvider
import cu.video.app.streamingcuba.streaming.track.TrackSelectionDialog
import cu.video.app.streamingcuba.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExoPlayerFragment : BaseFragment(), View.OnClickListener, StyledPlayerControlView.VisibilityListener {

    private var url: String? = null

    private lateinit var viewModel: PlayerViewModel

    // Saved instance state keys.
    companion object {
        private const val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
        private const val KEY_AUTO_PLAY = "auto_play"
    }

    //views
    private lateinit var playerV: StyledPlayerView
    private lateinit var controlsRoot: FrameLayout
    private lateinit var selectTracksBtn: ImageButton
    private lateinit var loadingView: LottieAnimationView
    private lateinit var backBtn: ImageButton
    private lateinit var selectScreenBtn: ImageButton

    //exo player
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var mediaSource: MediaSource? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: Parameters? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null

    private var startAutoPlay = true
    private var isShowingTrackSelectionDialog = false

    // Fragment lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExoPlayerBinding.inflate(layoutInflater, container, false)

        url = arguments?.getString("url")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        if (savedInstanceState != null) {
            trackSelectorParameters =
                savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
        } else {
            clearStartPosition()
        }
        configView(binding)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            selectScreenBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_fullscreen_exit_24))
        } else {
            selectScreenBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_fullscreen_24))
        }
    }

    override fun configView(vb: ViewBinding) {
        super.configView(vb)

        (vb as FragmentExoPlayerBinding).apply {
            controlsRoot = controlsRootView as FrameLayout
            selectTracksBtn = selectTracksButton
            playerV = playerView
            loadingView = loadLottie
            backBtn = backButton
            selectScreenBtn = selectScreenButton
        }

        backBtn.setOnClickListener(this)
        selectScreenBtn.setOnClickListener(this)
        selectTracksBtn.setOnClickListener(this)
        playerV.setControllerVisibilityListener(this)

        playerV.setErrorMessageProvider(PlayerErrorMessageProvider())
        playerV.requestFocus()
    }

    // Fragment lifecycle

    override fun onStart() {
        super.onStart()
        if (url != null) initializePlayer()
        if (this::playerV.isInitialized) {
            playerV.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (simpleExoPlayer == null && url != null) {
            initializePlayer()
            if (this::playerV.isInitialized) {
                playerV.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::playerV.isInitialized) {
            playerV.onResume()
        }
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (this::playerV.isInitialized) {
            playerV.onResume()
        }
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()
        updateStartPosition()
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters)
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay)
    }

    // OnClickListener methods
    override fun onClick(view: View) {
        when (view) {
            selectTracksBtn -> {
                if (!isShowingTrackSelectionDialog
                    && TrackSelectionDialog.willHaveContent(trackSelector!!)
                ) {
                    showTrackSelector()
                }
            }
            selectScreenBtn -> {
                if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    handleFullScreen(true)
                } else {
                    handleFullScreen(false)
                }
            }
            backBtn -> {
                if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    handleFullScreen(true)
                } else navController.navigateUp()
            }
        }

        if (view == selectTracksBtn
            && !isShowingTrackSelectionDialog
            && TrackSelectionDialog.willHaveContent(trackSelector!!)
        ) {
            showTrackSelector()
        }
    }

    private fun handleFullScreen(full: Boolean){
        if (full) {
            requireActivity().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
            } else requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            requireActivity().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun showTrackSelector() {
        if (trackSelector?.currentMappedTrackInfo != null) {
            isShowingTrackSelectionDialog = true
            val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(
                trackSelector!!
            ) {
                isShowingTrackSelectionDialog = false
            }
            trackSelectionDialog.show(childFragmentManager, null)
        }
    }

    // PlaybackControlView.VisibilityListener implementation
    override fun onVisibilityChange(visibility: Int) {
        controlsRoot.visibility = visibility
    }

    // Internal methods
    private fun initializePlayer() {

        /***
        //rtmp mode
        //URL: rtmp://stmv2.zcastbr.com:1935/streamingcuba?streamingcuba&n1tuLNOSnE/live
        //parameters: user&pass/streamingName
        //Start->

        val dataSourceFactory = RtmpDataSourceFactory()

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

        //end->
         ***/

        val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(requireContext())

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), getString(R.string.app_name)), bandwidthMeter
        )
        //hls mode working with errors on using Transcoder
        //Start->

        var testUrl = "https://www.webmedialive.org:1936/8060/ngrp:8060_all/playlist.m3u8"

        // Create a HLS media source pointing to a playlist uri.
        val videoSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(testUrl)))

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
        //end->

        trackSelector = DefaultTrackSelector(
            requireContext(),
            videoTrackSelectionFactory
        )

        if (trackSelectorParameters == null) {
            trackSelectorParameters = trackSelector?.buildUponParameters()?.build()!!
        }

        trackSelector?.parameters = trackSelectorParameters!!

        if (simpleExoPlayer == null) {
            simpleExoPlayer = SimpleExoPlayer.Builder(requireContext())
                .setTrackSelector(trackSelector!!)
                .setBandwidthMeter(DefaultBandwidthMeter.getSingletonInstance(requireContext()))
                .build()

            simpleExoPlayer?.addListener(PlayerEventListener())
            simpleExoPlayer?.playWhenReady = startAutoPlay
            simpleExoPlayer?.addAnalyticsListener(EventLogger(trackSelector))
            playerV.player = simpleExoPlayer
        }
        simpleExoPlayer?.addMediaSource(videoSource)
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = true

    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            updateTrackSelectorParameters()
            updateStartPosition()
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            mediaSource = null
            trackSelector = null
        }
    }

    private fun updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector?.parameters
        }
    }

    private fun updateStartPosition() {
        if (simpleExoPlayer != null) {
            startAutoPlay = simpleExoPlayer?.playWhenReady!!
        }
    }

    private fun clearStartPosition() {
        startAutoPlay = true
    }

    private fun updateButtonVisibility() {
        selectTracksBtn.isEnabled = simpleExoPlayer != null &&
                TrackSelectionDialog.willHaveContent(trackSelector!!)
    }

    private fun showControls() {
        controlsRoot.visibility = View.VISIBLE
    }

    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false
        }
        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) {
                return true
            }
            cause = cause.cause
        }
        return false
    }

    inner class PlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    showControls()
                }
                Player.STATE_READY -> {
                    loadingView.visibility = View.GONE
                }
                Player.STATE_BUFFERING -> {
                    loadingView.visibility = View.VISIBLE
                }
                Player.STATE_IDLE -> {
                }
            }
            updateButtonVisibility()
        }

        override fun onPlayerError(e: ExoPlaybackException) {
            if (isBehindLiveWindow(e) && url != null) {
                clearStartPosition()
                initializePlayer()
            } else {
                updateButtonVisibility()
                showControls()
            }
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            updateButtonVisibility()
            if (trackGroups !== lastSeenTrackGroupArray) {
                val mappedTrackInfo = trackSelector?.currentMappedTrackInfo
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
                    ) {
                        showToast("Video no soportado")
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                        == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
                    ) {
                        showToast("Audio no soportado")
                    }
                }
                lastSeenTrackGroupArray = trackGroups
            }
        }
    }

}