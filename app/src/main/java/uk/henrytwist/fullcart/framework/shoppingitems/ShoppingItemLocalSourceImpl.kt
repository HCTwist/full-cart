package uk.henrytwist.fullcart.framework.shoppingitems

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.fullcart.data.TimeConverter
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemDetailsModel
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemLocalSource
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemSummaryModel
import uk.henrytwist.fullcart.models.NewShoppingItem
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.ShoppingItemCount
import java.time.LocalDateTime
import javax.inject.Inject

class ShoppingItemLocalSourceImpl @Inject constructor(private val dao: ShoppingItemDao) : ShoppingItemLocalSource {

    override fun getCounts(): Flow<List<ShoppingItemCount>> {

        return dao.getCounts()
    }

    override fun getSummariesFor(listId: Int): Flow<List<ShoppingItemSummaryModel>> {

        return dao.getSummariesFor(listId).map { list ->

            list.map {

                it.toModel()
            }
        }
    }

    override suspend fun add(item: NewShoppingItem) {

        dao.insert(item.let {

            ShoppingItemEntity(
                    0,
                    it.listId,
                    it.name,
                    it.category?.id,
                    it.quantity.number,
                    it.quantity.unit.ordinal,
                    it.checked,
                    TimeConverter.toEpochSeconds(item.checkedTime)
            )
        })
    }

    override suspend fun setChecked(id: Int, checked: Boolean, time: LocalDateTime?) {

        dao.setChecked(id, checked, TimeConverter.toEpochSeconds(time))
    }

    override suspend fun removeAll(id: List<Int>) {

        dao.removeAll(id)
    }

    override suspend fun removeAllFrom(listId: Int) {

        dao.removeAllFrom(listId)
    }

    override suspend fun getCheckedTimes(): List<Pair<Int, LocalDateTime>> {

        return dao.getCheckedTimes().map {

            it.id to TimeConverter.toLocalDateTime(it.checkedTime)
        }
    }

    override suspend fun get(id: Int): ShoppingItemDetailsModel {

        return dao.get(id).let {

            ShoppingItemDetailsModel(
                    it.id,
                    it.listId,
                    it.name,
                    it.category,
                    Quantity(it.quantityNumber, Quantity.Unit.values()[it.quantityUnit]),
                    it.checked
            )
        }
    }

    override suspend fun edit(model: ShoppingItemDetailsModel) {

        dao.update(model.id, model.listId, model.name, model.category, model.quantity.number, model.quantity.unit.ordinal)
    }

    override suspend fun moveToList(model: ShoppingItemSummaryModel, newListId: Int) {

        dao.updateList(model.id, newListId)
    }

    override suspend fun removeCategory(categoryId: Int) {

        dao.removeCategory(categoryId)
    }

    private fun ShoppingItemSummaryEntity.toModel(): ShoppingItemSummaryModel {

        val quantity = Quantity(quantityNumber, Quantity.Unit.values()[quantityUnit])
        return ShoppingItemSummaryModel(id, name, category, quantity, checked)
    }
}