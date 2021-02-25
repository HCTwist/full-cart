package uk.henrytwist.fullcart.framework

import android.content.SharedPreferences
import android.content.res.Resources
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.models.CategoryColor
import uk.henrytwist.fullcart.models.NewCategory
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(private val resources: Resources, @SharedPreferencesModule.Data private val dataPreferences: SharedPreferences) : OnboardingRepository {

    override fun isFirstLaunch(): Boolean {

        return dataPreferences.getBoolean(FIRST_TIME, true)
    }

    override fun setNotFirstLaunch() {

        dataPreferences.edit().putBoolean(FIRST_TIME, false).apply()
    }

    override fun hasExploredMenu(): Boolean {

        return dataPreferences.getBoolean(EXPLORED_MENU, false)
    }

    override fun setHasExploredMenu() {

        dataPreferences.edit().putBoolean(EXPLORED_MENU, true).apply()
    }

    override fun getInitialShoppingListName(): String {

        return resources.getString(R.string.default_shopping_list_name)
    }

    override fun getInitialPantryName(): String {

        return resources.getString(R.string.default_pantry_name)
    }

    override fun getInitialCategories(): List<NewCategory> {

        return listOf(
                newCat(CategoryColor.FRUIT, R.string.category_name_fruit),
                newCat(CategoryColor.VEGETABLES, R.string.category_name_vegetables),
                newCat(CategoryColor.FISH, R.string.category_name_fish),
                newCat(CategoryColor.MEAT, R.string.category_name_meat),
                newCat(CategoryColor.BAKERY, R.string.category_name_bakery),
                newCat(CategoryColor.DRINKS, R.string.category_name_drinks),
                newCat(CategoryColor.TREATS, R.string.category_name_treats),
                newCat(CategoryColor.HOUSEHOLD, R.string.category_name_household),
                newCat(CategoryColor.FREEZER, R.string.category_name_freezer),
                newCat(CategoryColor.PERSONAL_CARE, R.string.category_name_personal_care),
                newCat(CategoryColor.HERBS_SPICES, R.string.category_name_herbs_spices),
                newCat(CategoryColor.BABY, R.string.category_name_baby),
                newCat(CategoryColor.DAIRY, R.string.category_name_dairy)
        )
    }

    private fun newCat(color: CategoryColor, nameRes: Int): NewCategory {

        return NewCategory(resources.getString(nameRes), color.colorHex, false)
    }

    companion object {

        const val FIRST_TIME = "first_time"
        const val EXPLORED_MENU = "explored_menu"
    }
}