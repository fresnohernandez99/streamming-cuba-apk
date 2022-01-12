/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cu.video.app.streamingcuba.streaming.track

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.util.Assertions
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import cu.video.app.streamingcuba.R
import java.util.*

/** Dialog to select tracks.  */
class TrackSelectionDialog : DialogFragment() {

    private val tabFragments: SparseArray<TrackSelectionViewFragment> = SparseArray<TrackSelectionViewFragment>()
    private val tabTrackTypes: ArrayList<Int> = ArrayList()
    private var titleId = 0
    private var onClickListener: DialogInterface.OnClickListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null

    private fun init(
        titleId: Int,
        mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
        initialParameters: DefaultTrackSelector.Parameters,
        allowAdaptiveSelections: Boolean,
        allowMultipleOverrides: Boolean,
        onClickListener: DialogInterface.OnClickListener,
        onDismissListener: DialogInterface.OnDismissListener
    ) {
        this.titleId = titleId
        this.onClickListener = onClickListener
        this.onDismissListener = onDismissListener
        for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
            if (showTabForRenderer(mappedTrackInfo, rendererIndex)) {
                val trackType: Int = mappedTrackInfo.getRendererType(rendererIndex)
                val trackGroupArray: TrackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
                val tabFragment = TrackSelectionViewFragment()
                tabFragment.init(
                    mappedTrackInfo,
                    rendererIndex,
                    initialParameters.getRendererDisabled(rendererIndex),
                    initialParameters.getSelectionOverride(rendererIndex, trackGroupArray),
                    allowAdaptiveSelections,
                    allowMultipleOverrides
                )
                tabFragments.put(rendererIndex, tabFragment)
                tabTrackTypes.add(trackType)
            }
        }
    }

    /**
     * Returns whether a renderer is disabled.
     *
     * @param rendererIndex Renderer index.
     * @return Whether the renderer is disabled.
     */
    fun getIsDisabled(rendererIndex: Int): Boolean {
        val rendererView: TrackSelectionViewFragment? = tabFragments.get(rendererIndex)
        return rendererView != null && rendererView.isDisabled
    }

    /**
     * Returns the list of selected track selection overrides for the specified renderer. There will
     * be at most one override for each track group.
     *
     * @param rendererIndex Renderer index.
     * @return The list of track selection overrides for this renderer.
     */
    fun getOverrides(rendererIndex: Int): List<DefaultTrackSelector.SelectionOverride> {
        val rendererView: TrackSelectionViewFragment? = tabFragments.get(rendererIndex)
        return if (rendererView == null) emptyList() else rendererView.overrides!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // We need to own the view to let tab layout work correctly on all API levels. We can't use
        // AlertDialog because it owns the view itself, so we use AppCompatDialog instead, themed using
        // the AlertDialog theme overlay with force-enabled title.
        val dialog = AppCompatDialog(activity, R.style.TrackSelectionDialogThemeOverlay)
        dialog.setTitle(titleId)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dialogView: View = inflater.inflate(R.layout.track_selection_dialog, container, false)
        val tabLayout: TabLayout = dialogView.findViewById(R.id.track_selection_dialog_tab_layout)
        val viewPager: ViewPager = dialogView.findViewById(R.id.track_selection_dialog_view_pager)
        val cancelButton =
            dialogView.findViewById<Button>(R.id.track_selection_dialog_cancel_button)
        val okButton = dialogView.findViewById<Button>(R.id.track_selection_dialog_ok_button)
        viewPager.adapter = FragmentAdapter(
            childFragmentManager,
            requireContext(),
            tabFragments,
            tabTrackTypes
        )
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.visibility = if (tabFragments.size() > 1) View.VISIBLE else View.GONE
        cancelButton.setOnClickListener { dismiss() }
        okButton.setOnClickListener {
            onClickListener?.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
            dismiss()
        }
        return dialogView
    }

    companion object {
        private const val TAG = "TrackSelectionDialog"

        /**
         * Returns whether a track selection dialog will have content to display if initialized with the
         * specified [DefaultTrackSelector] in its current state.
         */
        fun willHaveContent(trackSelector: DefaultTrackSelector): Boolean {
            val mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? =
                trackSelector.currentMappedTrackInfo
            return mappedTrackInfo != null && willHaveContent(mappedTrackInfo)
        }

        /**
         * Returns whether a track selection dialog will have content to display if initialized with the
         * specified [MappedTrackInfo].
         */
        fun willHaveContent(mappedTrackInfo: MappingTrackSelector.MappedTrackInfo): Boolean {
            for (i in 0 until mappedTrackInfo.rendererCount) {
                if (showTabForRenderer(mappedTrackInfo, i)) {
                    return true
                }
            }
            return false
        }

        //IM USING THIS
        /**
         * Creates a dialog for a given [DefaultTrackSelector], whose parameters will be
         * automatically updated when tracks are selected.
         *
         * @param trackSelector The [DefaultTrackSelector].
         * @param onDismissListener A [DialogInterface.OnDismissListener] to call when the dialog is
         * dismissed.
         */
        fun createForTrackSelector(
            trackSelector: DefaultTrackSelector,
            onDismissListener: DialogInterface.OnDismissListener
        ): TrackSelectionDialog {
            val mappedTrackInfo: MappingTrackSelector.MappedTrackInfo =
                Assertions.checkNotNull<MappingTrackSelector.MappedTrackInfo>(trackSelector.currentMappedTrackInfo)
            val trackSelectionDialog = TrackSelectionDialog()
            val parameters: DefaultTrackSelector.Parameters = trackSelector.parameters
            trackSelectionDialog.init( /* titleId= */
                R.string.track_selection_title,
                mappedTrackInfo,  /* initialParameters = */
                parameters,
                allowAdaptiveSelections = true,
                allowMultipleOverrides = false,
                onClickListener = DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    val builder: DefaultTrackSelector.ParametersBuilder = parameters.buildUpon()
                    for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                        builder.clearSelectionOverrides(rendererIndex).setRendererDisabled(
                            rendererIndex,
                            trackSelectionDialog.getIsDisabled(rendererIndex)
                        )
                        val overrides: List<DefaultTrackSelector.SelectionOverride> =
                            trackSelectionDialog.getOverrides(rendererIndex)
                        if (overrides.isNotEmpty()) {
                            Log.d(
                                TAG, "override: " + Gson().toJson(
                                    overrides[0]
                                )
                            )
                            builder.setSelectionOverride(
                                rendererIndex,
                                mappedTrackInfo.getTrackGroups(rendererIndex),
                                overrides[0]
                            )
                        }
                    }
                    trackSelector.setParameters(builder)
                },
                onDismissListener = onDismissListener
            )
            return trackSelectionDialog
        }

        /**
         * Creates a dialog for given [MappedTrackInfo] and [DefaultTrackSelector.Parameters].
         *
         * @param titleId The resource id of the dialog title.
         * @param mappedTrackInfo The [MappedTrackInfo] to display.
         * @param initialParameters The [DefaultTrackSelector.Parameters] describing the initial
         * track selection.
         * @param allowAdaptiveSelections Whether adaptive selections (consisting of more than one track)
         * can be made.
         * @param allowMultipleOverrides Whether tracks from multiple track groups can be selected.
         * @param onClickListener [DialogInterface.OnClickListener] called when tracks are selected.
         * @param onDismissListener [DialogInterface.OnDismissListener] called when the dialog is
         * dismissed.
         */
        fun createForMappedTrackInfoAndParameters(
            titleId: Int,
            mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
            initialParameters: DefaultTrackSelector.Parameters,
            allowAdaptiveSelections: Boolean,
            allowMultipleOverrides: Boolean,
            onClickListener: DialogInterface.OnClickListener,
            onDismissListener: DialogInterface.OnDismissListener
        ): TrackSelectionDialog {
            val trackSelectionDialog = TrackSelectionDialog()
            trackSelectionDialog.init(
                titleId,
                mappedTrackInfo,
                initialParameters,
                allowAdaptiveSelections,
                allowMultipleOverrides,
                onClickListener,
                onDismissListener
            )
            return trackSelectionDialog
        }

        fun showTabForRenderer(mappedTrackInfo: MappingTrackSelector.MappedTrackInfo, rendererIndex: Int): Boolean {
            val trackGroupArray: TrackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
            if (trackGroupArray.length == 0) {
                return false
            }
            val trackType: Int = mappedTrackInfo.getRendererType(rendererIndex)
            return isSupportedTrackType(trackType)
        }

        private fun isSupportedTrackType(trackType: Int): Boolean {
            return when (trackType) {
                C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO, C.TRACK_TYPE_TEXT -> true
                else -> false
            }
        }
    }

    init {
        // Retain instance across activity re-creation to prevent losing access to init data.
        retainInstance = true
    }
}