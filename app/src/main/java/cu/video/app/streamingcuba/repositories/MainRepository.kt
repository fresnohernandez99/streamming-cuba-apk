package cu.video.app.streamingcuba.repositories

import androidx.annotation.WorkerThread
import cu.video.app.streamingcuba.api.ApiClient
import cu.video.app.streamingcuba.data.models.entities.Event
import cu.video.app.streamingcuba.data.models.entities.Streaming
import cu.video.app.streamingcuba.persistence.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val eventDao: EventDao
) : Repository {

    @WorkerThread
    fun fetchUpcomingEvents(
        onSuccess: (streamings: List<Streaming>) -> Unit,
        onError: () -> Unit
    ) = flow<List<Event>?> {
        val upcomingEventsResponse = apiClient.getUpComingEvent()

        val successful =
            if (upcomingEventsResponse != null && upcomingEventsResponse.isSuccessful) {
                eventDao.cleanEvents()
                upcomingEventsResponse.body()?.box?.let {
                    eventDao.insert(it.upcomings)
                }
                true
            } else false

        val upcomingEventsOnDB = eventDao.getEvents()
        emit(upcomingEventsOnDB)

        if (successful) onSuccess(upcomingEventsResponse?.body()?.box?.onLive!!)
        else onError()

    }.flowOn(Dispatchers.IO)
}