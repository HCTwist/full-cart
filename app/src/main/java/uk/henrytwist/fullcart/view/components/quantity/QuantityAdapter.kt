package uk.henrytwist.fullcart.view.components.quantity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.R
import uk.henrytwist.selectslider.SelectSliderView

abstract class QuantityAdapter : SelectSliderView.Adapter<QuantityAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.quantity_number_row, parent, false) as TextView)
    }

    override fun onBindSliderViewHolder(holder: Holder, position: Int) {

        holder.bind(getText(holder.itemView.context, position))
    }

    override fun onAnimateViewHolder(holder: Holder, selectedFraction: Float) {

        holder.animateSelection(selectedFraction)
    }

    abstract fun getText(context: Context, position: Int): String

    class Holder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {

        fun bind(text: String) {

            textView.text = text
        }

        fun animateSelection(selection: Float) {

            textView.alpha = (selection + 0.5F) / 1.5F
        }
    }
}