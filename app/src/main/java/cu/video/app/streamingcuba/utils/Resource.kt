package cu.video.app.streamingcuba.utils

import cu.video.app.streamingcuba.utils.enums.Status

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> notRequested(): Resource<T> {
            return Resource(Status.NOT_REQUESTED, null, null)
        }

    }
}
