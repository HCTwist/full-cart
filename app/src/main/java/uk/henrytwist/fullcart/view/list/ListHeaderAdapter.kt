package uk.henrytwist.fullcart.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.ListHeaderBinding

class ListHeaderAdapter : RecyclerView.Adapter<ListHeaderAdapter.Holder>() {

    var title = ""

    lateinit var handler: Handler

    var showTouchPrompt = false

    override fun getItemCount(): Int {

        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(title, showTouchPrompt, handler)
    }

    class Holder(private val binding: ListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, showTouchPrompt: Boolean, handler: Handler) {

            binding.title = title
            binding.showTouchPrompt = showTouchPrompt
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onTitleClick()

        fun onMenuClick()
    }
}