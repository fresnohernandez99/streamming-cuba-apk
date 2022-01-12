package cu.video.app.streamingcuba.persistence

import androidx.room.*
import cu.video.app.streamingcuba.data.models.entities.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(events: List<Event>)

    @Query("SELECT * FROM event")
    suspend fun getEvents(): List<Event>

    @Query("DELETE FROM event")
    suspend fun cleanEvents()
}