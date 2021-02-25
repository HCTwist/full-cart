package uk.henrytwist.fullcart.framework.searchitems

import androidx.room.Entity
import androidx.room.Fts4

@Entity
@Fts4(contentEntity = SearchItemEntity::class)
class SearchItemFTSEntity(val name: String)