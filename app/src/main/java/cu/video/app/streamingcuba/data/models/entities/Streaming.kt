package cu.video.app.streamingcuba.data.models.entities

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Streaming {
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0

    @Expose
    @SerializedName("type")
    var type: String = ""

    @Expose
    @SerializedName("title")
    var title: String = ""

    @Expose
    @SerializedName("description")
    var description: String = ""

    @Expose
    @SerializedName("img_url")
    var imgUrl: String = ""

    @Expose
    @SerializedName("img_res")
    var imgRes: String = ""

    @Expose
    @SerializedName("url")
    var url: String = ""

    companion object {
        const val TYPE_FREE = "free"
        const val TYPE_PREMIUM = "premium"
    }

}