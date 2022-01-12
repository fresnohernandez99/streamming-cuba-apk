package cu.video.app.streamingcuba.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "event")
class Event {
    @PrimaryKey
    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Long = 0

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String = ""

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String = ""

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String = ""

    @Expose
    @SerializedName("date")
    @ColumnInfo(name = "date")
    var date: String = ""

    @Expose
    @SerializedName("img_url")
    @ColumnInfo(name = "img_url")
    var imgUrl: String = ""
}