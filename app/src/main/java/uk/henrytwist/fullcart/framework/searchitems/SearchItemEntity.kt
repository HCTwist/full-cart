package uk.henrytwist.fullcart.framework.searchitems

import androidx.room.Entity

@Entity(primaryKeys = ["name", "category"])
class SearchItemEntity(
        val name: String,
        val category: Int,
        val count: Int,
        val lastUpdated: Long
)