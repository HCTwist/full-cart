package uk.henrytwist.fullcart.view.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.GenericHeaderBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.Holder>() {

    var title = ""
    lateinit var handler: Handler

    override fun getItemCount(): Int {

        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(GenericHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(title, handler)
    }

    class Holder(private val binding: GenericHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, handler: Handler) {

            binding.title = title
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onClickBack()
    }
}