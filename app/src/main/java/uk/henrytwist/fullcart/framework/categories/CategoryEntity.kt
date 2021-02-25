package uk.henrytwist.fullcart.framework.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CategoryEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val color: Int,
        val isDefault: Boolean
)