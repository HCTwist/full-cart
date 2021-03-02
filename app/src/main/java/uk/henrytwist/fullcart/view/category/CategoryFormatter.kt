package uk.henrytwist.fullcart.view.category

import android.content.Context
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.CategoryColor

object CategoryFormatter {

    fun resolveColorName(context: Context, color: CategoryColor): String {

        return context.getString(when (color) {

            CategoryColor.FRUIT -> R.string.category_color_fruit
            CategoryColor.VEGETABLES -> R.string.category_color_vegetables
            CategoryColor.FISH -> R.string.category_color_fish
            CategoryColor.MEAT -> R.string.category_color_meat
            CategoryColor.BAKERY -> R.string.category_color_bakery
            CategoryColor.DRINKS -> R.string.category_color_drinks
            CategoryColor.TREATS -> R.string.category_color_treats
            CategoryColor.HOUSEHOLD -> R.string.category_color_household
            CategoryColor.FREEZER -> R.string.category_color_freezer
            CategoryColor.PERSONAL_CARE -> R.string.category_color_personal_care
            CategoryColor.HERBS_SPICES -> R.string.category_color_herbs_spices
            CategoryColor.BABY -> R.string.category_color_baby
            CategoryColor.DAIRY -> R.string.category_color_dairy
        })
    }


}