package cu.video.app.streamingcuba.streaming.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.ui.TrackSelectionView
import cu.video.app.streamingcuba.R
import java.util.Comparator

/** Fragment to show a track selection in tab of the track selection dialog.  */
class TrackSelectionViewFragment : Fragment(), TrackSelectionView.TrackSelectionListener,
    Comparator<Format> {

    private var mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? = null
    private var rendererIndex = 0
    private var allowAdaptiveSelections = false
    private var allowMultipleOverrides = false

    /* package */
    var isDisabled = false

    /* package */
    var overrides: List<DefaultTrackSelector.SelectionOverride>? = null
    fun init(
        mappedTrackInfo: MappingTrackSelector.MappedTrackInfo?,
        rendererIndex: Int,
        initialIsDisabled: Boolean,
        initialOverride: DefaultTrackSelector.SelectionOverride?,
        allowAdaptiveSelections: Boolean,
        allowMultipleOverrides: Boolean
    ) {
        this.mappedTrackInfo = mappedTrackInfo
        this.rendererIndex = rendererIndex
        isDisabled = initialIsDisabled
        overrides = if (initialOverride == null) emptyList() else listOf(initialOverride)
        this.allowAdaptiveSelections = allowAdaptiveSelections
        this.allowMultipleOverrides = allowMultipleOverrides
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(
            R.layout.exo_track_selection_dialog,
            container,  /* attachToRoot= */
            false
        )
        val trackSelectionView: TrackSelectionView =
            rootView.findViewById(R.id.exo_track_selection_view)
        trackSelectionView.setShowDisableOption(true)
        trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides)
        trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections)
        trackSelectionView.init(
            mappedTrackInfo!!,
            rendererIndex,
            isDisabled,
            overrides!!,  /* listener= */
            this,
            this
        )
        return rootView
    }

    override fun onTrackSelectionChanged(
        isDisabled: Boolean,
        overrides: List<DefaultTrackSelector.SelectionOverride>
    ) {
        this.isDisabled = isDisabled
        this.overrides = overrides
    }

    init {
        // Retain instance across activity re-creation to prevent losing access to init data.
        retainInstance = true
    }

    override fun compare(o1: Format?, o2: Format?): Int {
        return 1
    }

}