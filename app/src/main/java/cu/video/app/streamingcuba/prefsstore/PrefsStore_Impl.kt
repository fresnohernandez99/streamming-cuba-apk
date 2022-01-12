package cu.video.app.streamingcuba.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val STORE_NAME = "app_navegando"

class PrefsStore_Impl @Inject constructor(@ApplicationContext context: Context) : PrefsStore {

    private val dataStore = context.createDataStore(name = STORE_NAME)

    //FirstTime
    override fun firstTime() = dataStore.data.catch { exception ->
        // dataStore.data throws an IOException if it can't read the data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.FIRST_TIME] ?: true }


    override suspend fun setFalseFirstTime() {
        dataStore.edit {
            it[PreferencesKeys.FIRST_TIME] = false
        }
    }
    //endregion

    //KEYS
    private object PreferencesKeys {
        val FIRST_TIME = booleanPreferencesKey("FIRST_TIME")
    }
    //endregion

}