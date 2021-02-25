package uk.henrytwist.fullcart.framework

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.henrytwist.fullcart.framework.categories.CategoryDao
import uk.henrytwist.fullcart.framework.categories.CategoryEntity
import uk.henrytwist.fullcart.framework.pantryitems.PantryItemDao
import uk.henrytwist.fullcart.framework.pantryitems.PantryItemEntity
import uk.henrytwist.fullcart.framework.searchitems.SearchItemDao
import uk.henrytwist.fullcart.framework.searchitems.SearchItemEntity
import uk.henrytwist.fullcart.framework.searchitems.SearchItemFTSEntity
import uk.henrytwist.fullcart.framework.shoppingitems.ShoppingItemDao
import uk.henrytwist.fullcart.framework.shoppingitems.ShoppingItemEntity
import uk.henrytwist.fullcart.framework.listmeta.ListMetaDao
import uk.henrytwist.fullcart.framework.listmeta.ListMetaEntity

@Database(entities = [
    ListMetaEntity::class,
    ShoppingItemEntity::class,
    CategoryEntity::class,
    SearchItemEntity::class,
    SearchItemFTSEntity::class,
    PantryItemEntity::class
], version = 14, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ListMetaDao

    abstract fun shoppingItemDao(): ShoppingItemDao

    abstract fun categoryDao(): CategoryDao

    abstract fun searchItemDao(): SearchItemDao

    abstract fun pantryItemDao(): PantryItemDao
}