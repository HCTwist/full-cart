package uk.henrytwist.fullcart.view.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.OnboardingContentPageBinding
import uk.henrytwist.fullcart.databinding.OnboardingEndBinding

class OnboardingAdapter(private val pages: List<OnboardingPage>, private val onboardingEnd: OnboardingEnd, private val endHandler: EndHandler) : RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    override fun getItemCount(): Int {

        return pages.size + 1
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < pages.size) VIEW_TYPE_CONTENT else VIEW_TYPE_END
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            VIEW_TYPE_CONTENT -> Holder.Content(OnboardingContentPageBinding.inflate(inflater, parent, false))
            VIEW_TYPE_END -> Holder.End(OnboardingEndBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        when (holder) {

            is Holder.Content -> holder.bind(pages[position])
            is Holder.End -> holder.bind(onboardingEnd, endHandler)
        }
    }

    sealed class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class Content(private val binding: OnboardingContentPageBinding) : Holder(binding.root) {

            fun bind(page: OnboardingPage) {

                binding.page = page
                binding.executePendingBindings()
            }
        }

        class End(private val binding: OnboardingEndBinding) : Holder(binding.root) {

            fun bind(end: OnboardingEnd, handler: EndHandler) {

                binding.end = end
                binding.handler = handler
                binding.executePendingBindings()
            }
        }
    }

    interface EndHandler {

        fun onAddShoppingListCheckChanged(checked: Boolean)
        fun onAddPantryCheckChanged(checked: Boolean)
        fun onAddCategoriesCheckChanged(checked: Boolean)
    }

    companion object {

        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_END = 1
    }
}