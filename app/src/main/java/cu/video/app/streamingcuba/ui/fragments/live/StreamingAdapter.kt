package cu.video.app.streamingcuba.ui.fragments.live

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.data.models.entities.Streaming
import cu.video.app.streamingcuba.data.models.entities.Streaming.Companion.TYPE_FREE
import cu.video.app.streamingcuba.databinding.ItemStreamingBinding
import cu.video.app.streamingcuba.utils.RecyclerViewClickListener
import cu.video.app.streamingcuba.utils.imageLoader.ImageLoader

class StreamingAdapter(
    val imageLoader: ImageLoader,
    private val clickListener: RecyclerViewClickListener
) :
    RecyclerView.Adapter<StreamingAdapter.InnerViewHolder>() {

    private val items = ArrayList<Streaming>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =
            DataBindingUtil.inflate<ItemStreamingBinding>(
                inflater,
                R.layout.item_streaming,
                parent,
                false
            )

        return InnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
                clickListener.recyclerViewListClicked(
                    it,
                    adapterPosition,
                    items[adapterPosition].id,
                    items[adapterPosition].type,
                    items[adapterPosition].url
                )
            }
        }
    }

    fun setList(list: List<Streaming>) {
        items.clear()
        notifyDataSetChanged()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]

            if (item.type == TYPE_FREE) {
                premiumContainer.visibility = View.GONE
                freeContainer.visibility = View.VISIBLE
                imageLoader.loadResWithRoundedCorners(R.drawable.img_mock_1, itemImageFree, 20)
            } else {
                premiumContainer.visibility = View.VISIBLE
                freeContainer.visibility = View.GONE
                imageLoader.loadResWithRoundedCorners(R.drawable.img_mock_2, itemImagePre, 20)
            }
            actualItem = item

            executePendingBindings()
        }
    }

    override fun getItemCount() = items.size

    class InnerViewHolder(val binding: ItemStreamingBinding) :
        RecyclerView.ViewHolder(binding.root)
}