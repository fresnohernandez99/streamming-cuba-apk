package cu.video.app.streamingcuba.data.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {
    @Expose
    @SerializedName("code")
    val code: Int = 404

    @Expose
    @SerializedName("msg")
    val msg: String = ""

    @Expose
    @SerializedName("box")
    val box: T? = null
}