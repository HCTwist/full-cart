package uk.henrytwist.fullcart.data

import uk.henrytwist.fullcart.models.NewCategory

interface OnboardingRepository {

    fun isFirstLaunch(): Boolean

    fun setNotFirstLaunch()

    fun hasExploredMenu(): Boolean

    fun setHasExploredMenu()

    fun getInitialShoppingListName(): String

    fun getInitialPantryName(): String

    fun getInitialCategories(): List<NewCategory>
}