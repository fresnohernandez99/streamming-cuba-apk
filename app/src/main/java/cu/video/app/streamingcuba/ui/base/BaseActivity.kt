package cu.video.app.streamingcuba.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import cu.video.app.streamingcuba.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    var _binding: ViewBinding? = null
    val binding get() = _binding!!
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setTheme(R.style.Theme_StreamingCuba)
        super.onCreate(savedInstanceState, persistentState)
    }

    open fun configView(vb: ViewBinding) {
        navController = findNavController(R.id.nav_host_fragment)
    }
    open fun setListeners() {}
    open fun setObservers() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}