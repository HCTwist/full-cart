package uk.henrytwist.fullcart.framework.listmeta

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ListMetaEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val type: Int
)