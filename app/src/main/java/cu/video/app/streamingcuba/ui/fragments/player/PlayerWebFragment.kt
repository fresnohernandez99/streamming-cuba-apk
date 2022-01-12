package cu.video.app.streamingcuba.ui.fragments.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.databinding.FragmentPlayerWebBinding
import cu.video.app.streamingcuba.streaming.track.TrackSelectionDialog
import cu.video.app.streamingcuba.ui.base.BaseFragment
import cu.video.app.streamingcuba.ui.dialogs.Progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerWebFragment : BaseFragment(), View.OnClickListener {

    private var url: String? = null

    private lateinit var progress: Progress
    private var isLoaded: Boolean = false
    private var isControlsShowIn = true

    private lateinit var viewModel: PlayerViewModel

    //views
    private lateinit var selectScreenBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var rootV: FrameLayout
    private lateinit var playerView: WebView
    private lateinit var controlsContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerWebBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        url = arguments?.getString("url")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun configView(vb: ViewBinding) {
        super.configView(vb)

        (vb as FragmentPlayerWebBinding).apply {
            playerView = webView
            rootV = rootView
            backBtn = backButton
            selectScreenBtn = selectScreenButton
            controlsContainer = controls
        }

        backBtn.setOnClickListener(this)
        selectScreenBtn.setOnClickListener(this)
        /*
        playerView.setOnTouchListener { v, event ->
            showHideControls()
            true
        }*/

        playerView.settings.javaScriptEnabled = true
        if (!viewModel.isOnline()) {
            showNoNetSnackBar()
        }

        if (viewModel.isOnline() && !isLoaded && url != null) {
            Toast.makeText(requireContext(), "Comenzando", Toast.LENGTH_LONG).show()
            initialize()
        }
    }

    private fun initialize() {
        playerView.loadUrl(url!!)
        playerView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                setProgressDialogVisibility(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                setProgressDialogVisibility(false)
                playerView.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                showToast(errorMessage)
                setProgressDialogVisibility(false)
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun showHideControls() {
        if (isControlsShowIn) {
            isControlsShowIn = false
            ViewCompat.animate(controlsContainer).scaleY(0f).scaleX(0f).start()
        }
        else {
            ViewCompat.animate(controlsContainer).scaleY(1f).scaleX(1f).start()
            isControlsShowIn = true
        }
    }

    // OnClickListener methods
    override fun onClick(view: View) {
        when (view) {
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

    private fun setProgressDialogVisibility(visible: Boolean) {
        if (visible) progress = Progress(requireContext(), "Cargando", cancelable = true)
        progress.apply { if (visible) show() else dismiss() }
    }


    private fun showNoNetSnackBar() {
        val snack = Snackbar.make(rootV, "Sin internet", Snackbar.LENGTH_LONG)
        snack.setAction("Regresar") {
            navController.navigateUp()
        }
        snack.show()
    }
}