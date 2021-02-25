package uk.henrytwist.fullcart.data.currentlist

import javax.inject.Inject

class CurrentListRepository @Inject constructor(private val localSource: CurrentListLocalSource) {

    suspend fun get(): Int? {

        return localSource.get()
    }

    suspend fun set(listId: Int?) {

        localSource.set(listId)
    }
}