package uk.henrytwist.fullcart.framework

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Data

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Settings

    @Data
    @Provides
    fun provideDataSharedPreferences(@ApplicationContext context: Context): SharedPreferences {

        return context.getSharedPreferences("data", Context.MODE_PRIVATE)
    }

    @Settings
    @Provides
    fun provideSettingsSharedPreferences(@ApplicationContext context: Context): SharedPreferences {

        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}