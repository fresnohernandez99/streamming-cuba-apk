package cu.video.app.streamingcuba.streaming.track

import android.content.Context
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.exoplayer2.C
import cu.video.app.streamingcuba.R
import java.util.*

class FragmentAdapter(
    fragmentManager: FragmentManager,
    context: Context,
    private val tabFragments: SparseArray<TrackSelectionViewFragment>,
    private val tabTrackTypes: ArrayList<Int>
) : FragmentPagerAdapter(fragmentManager) {
    private val context: Context = context.applicationContext
    override fun getItem(position: Int): Fragment {
        return tabFragments.valueAt(position)
    }
    override fun getCount() = tabFragments.size()

    override fun getPageTitle(position: Int): CharSequence {
        return when (tabTrackTypes[position]) {
            C.TRACK_TYPE_VIDEO -> context.resources.getString(R.string.exo_track_selection_title_video)
            C.TRACK_TYPE_AUDIO -> context.resources.getString(R.string.exo_track_selection_title_audio)
            C.TRACK_TYPE_TEXT -> context.resources.getString(R.string.exo_track_selection_title_text)
            else -> throw IllegalArgumentException()
        }
    }

}