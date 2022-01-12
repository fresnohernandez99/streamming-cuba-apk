package cu.video.app.streamingcuba.data.models.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("identifier")
    var identifier: String = ""
)
