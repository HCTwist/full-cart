package uk.henrytwist.fullcart.framework

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AndroidModule {

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources
}