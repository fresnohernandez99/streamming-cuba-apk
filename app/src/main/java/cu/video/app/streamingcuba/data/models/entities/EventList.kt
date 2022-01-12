package cu.video.app.streamingcuba.data.models.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventList {
    @Expose
    @SerializedName("on_live")
    var onLive = ArrayList<Streaming>()

    @Expose
    @SerializedName("upcomings")
    var upcomings = ArrayList<Event>()

}