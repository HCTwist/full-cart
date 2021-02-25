package uk.henrytwist.fullcart.framework.pantryitems

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PantryItemEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val listId: Int,
        val name: String,
        val category: Int?,
        val quantityNumber: Int,
        val quantityUnit: Int,
        val useByDate: Long?
)