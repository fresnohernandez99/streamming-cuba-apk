package cu.video.app.streamingcuba.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    fun firstTime(): Flow<Boolean>

    suspend fun setFalseFirstTime()
}