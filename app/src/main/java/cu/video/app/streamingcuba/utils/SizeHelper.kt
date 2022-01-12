package cu.video.app.streamingcuba.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SizeHelper @Inject constructor(@ApplicationContext private val context: Context) {

    val xdpi = context.resources.displayMetrics.xdpi
    val ydpi = context.resources.displayMetrics.ydpi

    fun proportion(part: Float, dpi: Float) = dpi * part

}