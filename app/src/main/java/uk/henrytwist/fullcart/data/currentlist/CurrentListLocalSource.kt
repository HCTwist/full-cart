package uk.henrytwist.fullcart.data.currentlist

interface CurrentListLocalSource {

    suspend fun get(): Int?

    suspend fun set(listId: Int?)
}