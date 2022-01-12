package cu.video.app.streamingcuba.api

import android.util.Log
import cu.video.app.streamingcuba.data.models.response.UpcomingEventList
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun getUpComingEvent(): Response<UpcomingEventList>? {
        try {
            return apiService.getUpcomingEvent()
        } catch (e: Exception) {
            Log.e("ERROR", "Retrofit Exception")
        }
        return null
    }
/*
    //login
    suspend fun login(request: LoginRequest): Response<LoginResponse>? {
        try {
            return apiService.login(request)
        } catch (e: Exception) {
            Log.e("ERROR", "Retrofit Exception")
        }
        return null
    }
 */
}