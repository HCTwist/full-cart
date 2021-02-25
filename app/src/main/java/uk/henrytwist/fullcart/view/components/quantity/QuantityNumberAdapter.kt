package uk.henrytwist.fullcart.view.components.quantity

import android.content.Context
import uk.henrytwist.fullcart.models.Quantity

class QuantityNumberAdapter : QuantityAdapter() {

    lateinit var numberGenerator: Quantity.NumberGenerator

    override fun getItemCount(): Int {

        return Int.MAX_VALUE
    }

    override fun getText(context: Context, position: Int): String {

        return numberGenerator.generate(position).toString()
    }
}