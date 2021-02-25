package uk.henrytwist.fullcart.framework

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.data.categories.CategoryLocalSource
import uk.henrytwist.fullcart.data.currentlist.CurrentListLocalSource
import uk.henrytwist.fullcart.data.listmeta.ListMetaLocalSource
import uk.henrytwist.fullcart.data.pantryitems.PantryItemLocalSource
import uk.henrytwist.fullcart.data.searchitems.SearchItemLocalSource
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemLocalSource
import uk.henrytwist.fullcart.framework.categories.CategoryLocalSourceImpl
import uk.henrytwist.fullcart.framework.currentlist.CurrentListLocalSourceImpl
import uk.henrytwist.fullcart.framework.listmeta.ListMetaLocalSourceImpl
import uk.henrytwist.fullcart.framework.pantryitems.PantryItemLocalSourceImpl
import uk.henrytwist.fullcart.framework.searchitems.SearchItemLocalSourceImpl
import uk.henrytwist.fullcart.framework.settings.SettingsRepositoryAndroid
import uk.henrytwist.fullcart.framework.shoppingitems.ShoppingItemLocalSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLastScreenLocalSource(lastScreenLocalSourceImpl: CurrentListLocalSourceImpl): CurrentListLocalSource

    @Binds
    abstract fun bindShoppingListMetaLocalSource(shoppingListMetaLocalSourceImpl: ListMetaLocalSourceImpl): ListMetaLocalSource

    @Binds
    abstract fun bindShoppingItemLocalSource(shoppingItemLocalSourceImpl: ShoppingItemLocalSourceImpl): ShoppingItemLocalSource

    @Binds
    abstract fun bindCategoryLocalSource(categoryLocalSourceImpl: CategoryLocalSourceImpl): CategoryLocalSource

    @Binds
    abstract fun bindSearchItemLocalSource(searchItemLocalSourceImpl: SearchItemLocalSourceImpl): SearchItemLocalSource

    @Binds
    abstract fun bindPantryItemLocalSource(pantryItemLocalSourceImpl: PantryItemLocalSourceImpl): PantryItemLocalSource

    @Binds
    abstract fun bindSettingsRepository(settingsRepositoryAndroid: SettingsRepositoryAndroid): SettingsRepository

    @Binds
    abstract fun bindOnboardingRepository(onboardingRepository: OnboardingRepositoryImpl): OnboardingRepository
}