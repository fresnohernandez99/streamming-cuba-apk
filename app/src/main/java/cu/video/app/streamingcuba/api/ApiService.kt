package cu.video.app.streamingcuba.api

import cu.video.app.streamingcuba.data.URLFactory
import cu.video.app.streamingcuba.data.models.response.UpcomingEventList
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(URLFactory.upComingEvent)
    suspend fun getUpcomingEvent(): Response<UpcomingEventList>?
/*
    @POST(URLFactory.login)
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
*/

}