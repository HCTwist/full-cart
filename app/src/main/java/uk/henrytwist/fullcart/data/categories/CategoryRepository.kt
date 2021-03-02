package uk.henrytwist.fullcart.data.categories

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.NewCategory
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val localSource: CategoryLocalSource) {

    suspend fun add(category: NewCategory) {

        localSource.add(category)
    }

    fun getAll(): Flow<List<Category>> {

        return localSource.getAll()
    }

    suspend fun getColor(id: Int): Int {

        return localSource.getColor(id)
    }

    suspend fun get(id: Int): Category {

        return localSource.get(id)
    }

    suspend fun edit(category: Category) {

        localSource.edit(category)
    }

    suspend fun remove(id: Int) {

        localSource.remove(id)
    }
}