package cu.video.app.streamingcuba.ui.fragments.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.data.models.entities.Streaming
import cu.video.app.streamingcuba.data.models.mocks.Mocks
import cu.video.app.streamingcuba.databinding.FragmentLiveBinding
import cu.video.app.streamingcuba.ui.base.BaseFragment
import cu.video.app.streamingcuba.ui.dialogs.DialogSelectionClickListener
import cu.video.app.streamingcuba.ui.dialogs.Selection
import cu.video.app.streamingcuba.utils.RecyclerViewClickListener


class LiveFragment : BaseFragment(), View.OnClickListener,
    RecyclerViewClickListener {

    private lateinit var viewModel: LiveViewModel
    private lateinit var adapter: StreamingAdapter
    private lateinit var dscl: DialogSelectionClickListener

    //views
    private lateinit var listV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLiveBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(LiveViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configView(binding)
    }

    override fun configView(vb: ViewBinding) {
        super.configView(vb)
        (vb as FragmentLiveBinding).apply {
            listV = streamingList
        }

        adapter = StreamingAdapter(imageLoader, this)
        listV.adapter = adapter
        adapter.setList(Mocks.streamingsList)

    }

    override fun onClick(v: View) {
        val root: FragmentLiveBinding = (binding as FragmentLiveBinding)
        when (v) {

        }
    }

    override fun recyclerViewListClicked(
        V: View,
        pos: Int,
        id: Long,
        extraData1: Any?,
        extraData2: Any?
    ) {
        dscl = object : DialogSelectionClickListener {
            override fun dialogSelectionClicked(pos: Int, extraData1: Any?) {
                val bundle = bundleOf("url" to extraData2 as String)
                when (pos) {
                    //go exo player
                    0 -> {
                        navController.navigate(
                            R.id.action_startFragment_to_fullPlayerFragment,
                            //R.id.action_startFragment_to_exoPlayerFragment,
                            bundle
                        )
                    }
                    //go web player
                    1 -> {
                        navController.navigate(
                            R.id.action_startFragment_to_playerWebFragment,
                            bundle
                        )
                    }
                }
            }
        }
        //Receive: extraData1 : String => type  extraData2 : String => url

        when (extraData1 as String) {
            Streaming.TYPE_FREE -> {
                Selection(
                    requireContext(),
                    "Seleccione para visualizar",
                    arrayOf("Ver en reproductor", "Ver en la web"),
                    true,
                    dscl
                ).show()
            }
            Streaming.TYPE_PREMIUM -> {
                Toast.makeText(requireContext(), "Pronto estará disponible", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}