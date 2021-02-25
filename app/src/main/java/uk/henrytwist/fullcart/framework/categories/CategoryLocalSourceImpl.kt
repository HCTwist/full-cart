package uk.henrytwist.fullcart.framework.categories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.fullcart.data.categories.CategoryLocalSource
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.NewCategory
import javax.inject.Inject

class CategoryLocalSourceImpl @Inject constructor(private val dao: CategoryDao) : CategoryLocalSource {

    override suspend fun add(category: NewCategory) {

        category.let {

            dao.removeDefaultAndInsert(CategoryEntity(0, it.name, it.color, it.default))
        }
    }

    override fun getAll(): Flow<List<Category>> {

        return dao.getAll().map { list ->

            list.map {

                Category(it.id, it.name, it.color, it.isDefault)
            }
        }
    }

    override suspend fun getColor(id: Int): Int {

        return dao.getColor(id)
    }

    override suspend fun get(id: Int): Category {

        return dao.get(id).let {

            Category(it.id, it.name, it.color, it.isDefault)
        }
    }
}