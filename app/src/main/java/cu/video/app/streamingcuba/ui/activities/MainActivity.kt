package cu.video.app.streamingcuba.ui.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.databinding.ActivityMainBinding
import cu.video.app.streamingcuba.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configView(binding)
    }

    override fun configView(vb: ViewBinding) {
        super.configView(vb)

        (vb as ActivityMainBinding).apply {

        }
    }

    override fun onBackPressed() {
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars())
            } else window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        } else super.onBackPressed()
    }
}