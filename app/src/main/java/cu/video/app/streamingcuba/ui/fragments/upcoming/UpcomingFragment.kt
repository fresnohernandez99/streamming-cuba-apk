package cu.video.app.streamingcuba.ui.fragments.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cu.video.app.streamingcuba.data.models.mocks.Mocks
import cu.video.app.streamingcuba.databinding.FragmentUpcomingBinding
import cu.video.app.streamingcuba.ui.base.BaseFragment
import cu.video.app.streamingcuba.utils.RecyclerViewClickListener


class UpcomingFragment : BaseFragment(), View.OnClickListener,
    RecyclerViewClickListener {

    private lateinit var viewModel: UpcomingViewModel
    private lateinit var adapter: EventAdapter

    //views
    private lateinit var listV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configView(binding)
    }

    override fun configView(vb: ViewBinding) {
        super.configView(vb)
        (vb as FragmentUpcomingBinding).apply {
            listV = eventsList
        }

        listV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = EventAdapter(imageLoader, this)
        listV.adapter = adapter
        adapter.setList(Mocks.eventList)

    }

    override fun onClick(v: View) {
        val root: FragmentUpcomingBinding = (binding as FragmentUpcomingBinding)
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

    }
}