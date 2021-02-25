package uk.henrytwist.fullcart.framework

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, AppRoomDatabase::class.java, "local_database").build()

    @Provides
    fun provideShoppingListDao(roomDatabase: AppRoomDatabase) = roomDatabase.shoppingListDao()

    @Provides
    fun provideShoppingItemDao(roomDatabase: AppRoomDatabase) = roomDatabase.shoppingItemDao()

    @Provides
    fun provideCategoryDao(roomDatabase: AppRoomDatabase) = roomDatabase.categoryDao()

    @Provides
    fun provideSearchItemDao(roomDatabase: AppRoomDatabase) = roomDatabase.searchItemDao()

    @Provides
    fun providePantryItemDao(roomDatabase: AppRoomDatabase) = roomDatabase.pantryItemDao()
}