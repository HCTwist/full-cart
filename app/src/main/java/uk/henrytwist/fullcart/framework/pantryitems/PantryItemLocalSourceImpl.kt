package uk.henrytwist.fullcart.framework.pantryitems

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.fullcart.data.pantryitems.PantryItemDetailsModel
import uk.henrytwist.fullcart.data.pantryitems.PantryItemLocalSource
import uk.henrytwist.fullcart.data.pantryitems.PantryItemSummaryModel
import uk.henrytwist.fullcart.data.pantryitems.PantryUseByItemModel
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.UseByDate
import javax.inject.Inject

class PantryItemLocalSourceImpl @Inject constructor(private val dao: PantryItemDao) : PantryItemLocalSource {

    override fun getUseByDates(): Flow<List<UseByDate>> {

        return dao.getUseByDates().map { list ->

            list.map {

                UseByDate.fromEpochDay(it)
            }
        }
    }

    override fun getCount(): Flow<Int> {

        return dao.getCount()
    }

    override fun getSummariesFor(listId: Int): Flow<List<PantryItemSummaryModel>> {

        return dao.getSummariesFor(listId).map { list ->

            list.map {

                val quantity = quantity(it.quantityNumber, it.quantityUnit)
                val useByDate = useByDate(it.useByDate)
                PantryItemSummaryModel(it.id, it.name, it.category, quantity, useByDate)
            }
        }
    }

    override suspend fun add(item: NewPantryItem) {

        dao.add(item.toEntity())
    }

    override suspend fun addAll(items: List<NewPantryItem>) {

        dao.addAll(items.map { it.toEntity() })
    }

    override suspend fun removeAll(ids: List<Int>) {

        dao.deleteAll(ids)
    }

    override suspend fun removeAllFrom(listId: Int) {

        dao.deleteAllFrom(listId)
    }

    override suspend fun getUseByItems(): List<PantryUseByItemModel> {

        return dao.getUseByItems().map {

            PantryUseByItemModel(it.listId, it.name, UseByDate.fromEpochDay(it.useByDate))
        }
    }

    override suspend fun setQuantityNumber(id: Int, n: Int) {

        dao.updateQuantityNumber(id, n)
    }

    override suspend fun get(id: Int): PantryItemDetailsModel {

        return dao.get(id).let {

            PantryItemDetailsModel(
                    it.id,
                    it.listId,
                    it.name,
                    it.category,
                    quantity(it.quantityNumber, it.quantityUnit),
                    useByDate(it.useByDate)
            )
        }
    }

    override suspend fun edit(item: PantryItemDetailsModel) {

        dao.edit(item.id, item.listId, item.name, item.category, item.quantity.number, item.quantity.unit.ordinal, item.useByDate?.toEpochDay())
    }

    override suspend fun move(id: Int, listId: Int) {

        dao.move(id, listId)
    }

    override suspend fun removeCategory(categoryId: Int) {

        dao.removeCategory(categoryId)
    }

    private fun useByDate(useByDate: Long?) = if (useByDate != null) UseByDate.fromEpochDay(useByDate) else null

    private fun quantity(quantityNumber: Int, quantityUnit: Int) = Quantity(quantityNumber, Quantity.Unit.values()[quantityUnit])

    private fun NewPantryItem.toEntity() = PantryItemEntity(
            0,
            listId,
            name,
            category?.id,
            quantity.number,
            quantity.unit.ordinal,
            useByDate?.toEpochDay()
    )
}