package uk.henrytwist.fullcart.view.components.quantity

import android.content.Context
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.view.ListItemFormatter

class QuantityUnitAdapter : QuantityAdapter() {

    override fun getItemCount(): Int {

        return Quantity.Unit.values().size
    }

    override fun getText(context: Context, position: Int): String {

        return ListItemFormatter.getQuantityUnitString(context, Quantity.Unit.values()[position])
    }
}