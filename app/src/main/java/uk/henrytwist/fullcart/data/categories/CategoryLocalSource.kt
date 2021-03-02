package uk.henrytwist.fullcart.data.categories

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.NewCategory

interface CategoryLocalSource {

    suspend fun add(category: NewCategory)

    fun getAll(): Flow<List<Category>>

    suspend fun getColor(id: Int): Int

    suspend fun get(id: Int): Category

    suspend fun edit(category: Category)

    suspend fun remove(id: Int)
}