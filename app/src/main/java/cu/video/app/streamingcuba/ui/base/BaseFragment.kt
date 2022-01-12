package cu.video.app.streamingcuba.ui.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import cu.video.app.streamingcuba.utils.SizeHelper
import cu.video.app.streamingcuba.utils.imageLoader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    var _binding: ViewBinding? = null
    val binding get() = _binding!!
    lateinit var navController: NavController

    @Inject
    lateinit var sizeHelper: SizeHelper

    @Inject
    lateinit var imageLoader: ImageLoader

    open fun configView(vb: ViewBinding) {
        navController = vb.root.findNavController()
    }

    open fun setObservers() {}

    fun showToast(messageId: Int) {
        showToast(getString(messageId))
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard() {
        requireView().let { requireActivity().hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}