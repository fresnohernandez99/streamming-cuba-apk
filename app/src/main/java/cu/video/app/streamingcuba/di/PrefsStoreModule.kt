package cu.video.app.streamingcuba.di

import cu.video.app.streamingcuba.prefsstore.PrefsStore
import cu.video.app.streamingcuba.prefsstore.PrefsStore_Impl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrefsStoreModule {

    @Binds
    @Singleton
    abstract fun bindPrefsStore(prefsStoreImpl: PrefsStore_Impl): PrefsStore
}