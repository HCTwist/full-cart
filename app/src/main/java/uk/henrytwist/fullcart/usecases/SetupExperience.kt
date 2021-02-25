package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.models.NewList
import javax.inject.Inject

class SetupExperience @Inject constructor(
        private val listMetaRepository: ListMetaRepository,
        private val currentListRepository: CurrentListRepository,
        private val categoryRepository: CategoryRepository,
        private val onboardingRepository: OnboardingRepository
) {

    suspend operator fun invoke(addShoppingList: Boolean, addPantry: Boolean, addCategories: Boolean) {

        val shoppingListId = if (addShoppingList) listMetaRepository.add(NewList(onboardingRepository.getInitialShoppingListName(), ListType.SHOPPING_LIST)) else null
        val pantryId = if (addPantry) listMetaRepository.add(NewList(onboardingRepository.getInitialPantryName(), ListType.PANTRY)) else null

        if (shoppingListId != null) {

            currentListRepository.set(shoppingListId)
        } else if (pantryId != null) {

            currentListRepository.set(pantryId)
        }

        if (addCategories) {

            onboardingRepository.getInitialCategories().forEach {

                categoryRepository.add(it)
            }
        }

        onboardingRepository.setNotFirstLaunch()
    }
}