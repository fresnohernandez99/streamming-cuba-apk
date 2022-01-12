package cu.video.app.streamingcuba.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import cu.video.app.streamingcuba.data.models.entities.Event

@Database(entities = [Event::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun voteDao(): EventDao
}
