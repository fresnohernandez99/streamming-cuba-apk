package cu.video.app.streamingcuba.utils

import android.view.View

interface RecyclerViewClickListener {
    fun recyclerViewListClicked(V: View, pos: Int, id: Long, extraData1: Any?, extraData2: Any?)
}