package uk.henrytwist.fullcart.view

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import uk.henrytwist.androidbasics.getColorAttr
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.*
import uk.henrytwist.fullcart.view.list.shoppinglist.ShoppingListRow
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*


object ListItemFormatter {

    private val now by lazy { LocalDate.now() }

    fun resolveCategoryColor(context: Context, category: Category?) = category?.color
            ?: ContextCompat.getColor(context, R.color.no_category)

    fun resolveCategoryName(context: Context, category: Category?) = category?.name
            ?: context.getString(R.string.no_category)

    fun getUseByDateHint(context: Context, useByDate: UseByDate?): String {

        return if (useByDate == null) {

            context.getString(R.string.edit_item_use_by_hint)
        } else {

            context.getString(R.string.edit_item_use_by, useByDate.toFullString())
        }
    }

    fun getUseByDateSummary(resources: Resources, useByDate: UseByDate?): String? {

        if (useByDate == null) return null

        val d = now.until(useByDate.date, ChronoUnit.DAYS).toInt()

        return when {

            d < -7 -> resources.getString(R.string.over_a_week_ago)
            d in -7..-2 -> resources.getString(R.string.days_ago, -d)
            d == -1 -> resources.getString(R.string.yesterday)
            d == 0 -> resources.getString(R.string.today)
            d == 1 -> resources.getString(R.string.tomorrow)
            d in 2..7 -> useByDate.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            else -> useByDate.toFullString()
        }
    }

    fun getQuantityString(resources: Resources, quantity: Quantity): String {

        val n = quantity.number
        return when (quantity.unit) {

            Quantity.Unit.SINGLE -> resources.getString(R.string.quantity_single, n)
            Quantity.Unit.LITRES -> resources.getString(R.string.quantity_litres, n)
            Quantity.Unit.PINTS -> resources.getQuantityString(R.plurals.quantity_pints, n, n)
            Quantity.Unit.PACKS -> resources.getQuantityString(R.plurals.quantity_packs, n, n)
            Quantity.Unit.KILOGRAMS -> resources.getString(R.string.quantity_kilograms, n)
            Quantity.Unit.GRAMS -> resources.getString(R.string.quantity_grams, n)
            Quantity.Unit.ML -> resources.getString(R.string.quantity_ml, n)
        }
    }

    fun getQuantityUnitString(context: Context, unit: Quantity.Unit) = context.getString(when (unit) {


        Quantity.Unit.SINGLE -> R.string.quantity_single_unit
        Quantity.Unit.LITRES -> R.string.quantity_litres_unit
        Quantity.Unit.PINTS -> R.string.quantity_pints_unit
        Quantity.Unit.PACKS -> R.string.quantity_packs_unit
        Quantity.Unit.KILOGRAMS -> R.string.quantity_kilograms_unit
        Quantity.Unit.GRAMS -> R.string.quantity_grams_unit
        Quantity.Unit.ML -> R.string.quantity_ml_unit
    })

    fun buildItemTitle(context: Context, shoppingItem: ShoppingItemSummary): SpannableString {

        return buildItemTitle(context, shoppingItem.name, shoppingItem.quantity)
    }

    fun buildItemTitle(context: Context, pantryItem: PantryItemSummary): SpannableString {

        return buildItemTitle(context, pantryItem.name, pantryItem.quantity)
    }

    private fun buildItemTitle(context: Context, name: String, quantity: Quantity): SpannableString {

        if (quantity.isOneSingle()) {

            return SpannableString(name)
        }

        val quantitySuffix = context.getString(R.string.item_title_quantity_suffix, getQuantityString(context.resources, quantity))
        val string = SpannableString("$name$quantitySuffix")
        val color = context.getColorAttr(android.R.attr.textColorSecondary)
        val span = ForegroundColorSpan(color)

        string.setSpan(span, name.length, string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return string
    }

    fun resolveUseByDateTextColor(context: Context, useByDate: UseByDate?, toUseSoon: Boolean): Int {

        return if (useByDate == null || !toUseSoon) {

            context.getColorAttr(android.R.attr.textColorSecondary)
        } else {

            ContextCompat.getColor(context, if (useByDate.date.isBefore(now)) {

                R.color.to_use_overdue
            } else {

                R.color.to_use
            })
        }
    }

    fun resolveMoveToPantryButtonText(context: Context, divider: ShoppingListRow.Divider): String {

        return if (divider.singlePantryName == null) {

            context.getString(R.string.move_checked_to_pantry)
        } else {

            context.getString(R.string.move_checked_to_pantry_single, divider.singlePantryName)
        }
    }

    fun getShoppingItemShareText(resources: Resources, meta: ListMeta, items: List<ShoppingItemSummary>): String {

        val builder = StringBuilder()

        builder.append(meta.name)
        builder.append("\n")

        items.forEach {

            builder.append("\n")

            builder.append(resources.getString(
                    R.string.share_shopping_item,
                    it.name,
                    getQuantityString(resources, it.quantity)
            ))
        }

        return builder.toString()
    }

    fun getPantryItemShareText(resources: Resources, meta: ListMeta, items: List<PantryItemSummary>): String {

        val builder = StringBuilder()

        builder.append(meta.name)
        builder.append("\n")

        items.forEach {

            builder.append("\n")
            builder.append(if (it.hasUseByDate()) {

                resources.getString(
                        R.string.share_pantry_item_use_by,
                        it.name,
                        getQuantityString(resources, it.quantity),
                        getUseByDateSummary(resources, it.useByDate)
                )
            } else {

                resources.getString(
                        R.string.share_pantry_item,
                        it.name,
                        getQuantityString(resources, it.quantity)
                )
            })
        }

        return builder.toString()
    }
}