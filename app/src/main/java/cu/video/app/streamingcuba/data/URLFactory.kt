package cu.video.app.streamingcuba.data

import cu.video.app.streamingcuba.utils.enums.Environment

object URLFactory {

    private val host = Environment.DEVELOP

    fun getEntryPoint() = when (host) {
        Environment.DEVELOP -> "http://160.100.10.94:4004/"
        Environment.TEST -> ""
        Environment.PRODUCTION -> "https://navegando-api.herokuapp.com/"
    }

    const val upComingEvent = "upcoming-event"

}