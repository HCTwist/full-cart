package uk.henrytwist.fullcart.framework.shoppingitems

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ShoppingItemEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val listId: Int,
        val name: String,
        val category: Int?,
        val quantityNumber: Int,
        val quantityUnit: Int,
        val checked: Boolean,
        val checkedTime: Long?
)