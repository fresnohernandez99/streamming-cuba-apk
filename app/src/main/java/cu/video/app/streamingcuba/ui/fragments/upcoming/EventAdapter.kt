package cu.video.app.streamingcuba.ui.fragments.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.data.models.entities.Event
import cu.video.app.streamingcuba.data.models.mocks.Mocks
import cu.video.app.streamingcuba.databinding.ItemEventBinding
import cu.video.app.streamingcuba.utils.RecyclerViewClickListener
import cu.video.app.streamingcuba.utils.imageLoader.ImageLoader

class EventAdapter(
    val imageLoader: ImageLoader,
    var clickListener: RecyclerViewClickListener
) :
    RecyclerView.Adapter<EventAdapter.InnerViewHolder>() {

    private val items = ArrayList<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =
            DataBindingUtil.inflate<ItemEventBinding>(
                inflater,
                R.layout.item_event,
                parent,
                false
            )

        return InnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
            }
        }
    }

    fun setList(list: List<Event>) {
        items.clear()
        notifyDataSetChanged()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]

            imageLoader.loadResWithRoundedCorners(Mocks.arrayImg[position], itemImg, 20)
            actualItem = item

            executePendingBindings()
        }
    }

    override fun getItemCount() = items.size

    class InnerViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)
}