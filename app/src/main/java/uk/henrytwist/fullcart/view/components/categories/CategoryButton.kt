package uk.henrytwist.fullcart.view.components.categories

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.databinding.CategoryButtonRowBinding
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.view.ListItemFormatter

class CategoryButton(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet), View.OnClickListener {

    private val dotView: ImageView
    private val nameView: TextView

    private var showPopupWhenReady = false
    private var popup: ListPopupWindow? = null

    var categories: List<Category?>? = null
        set(value) {

            if (field == null && value != null && showPopupWhenReady) {
                showDialog(value)
            }
            field = value
        }

    var onCategorySelectedListener: OnCategorySelectedListener? = null

    init {

        orientation = HORIZONTAL
        minimumWidth = resources.getDimensionPixelSize(R.dimen.category_button_min_width)
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundResource(R.drawable.category_button)

        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.button_padding_horizontal)
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.button_padding_vertical)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        inflate(context, R.layout.category_button, this)

        dotView = findViewById(R.id.category_button_dot)
        nameView = findViewById(R.id.category_button_name)

        setOnClickListener(this)
    }

    fun setCategory(category: Category?) {

        dotView.imageTintList = ColorStateList.valueOf(ListItemFormatter.resolveCategoryColor(context, category))
        nameView.text = ListItemFormatter.resolveCategoryName(context, category)
    }

    override fun onClick(v: View?) {

        val categoriesVal = categories
        if (categoriesVal == null) {

            showPopupWhenReady = true
        } else {

            showDialog(categoriesVal)
        }
    }

    private fun showDialog(categories: List<Category?>) {

        val builder = MaterialAlertDialogBuilder(context)

        val adapter = PopupAdapter()
        adapter.categories = categories
        builder.setAdapter(adapter, null)

        val dialog = builder.create()
        adapter.handler = object : PopupAdapter.Handler {

            override fun onCategoryClicked(category: Category?) {

                onPopupCategoryClicked(category)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @Deprecated("Almost certainly replaced with showDialog")
    private fun showPopup(categories: List<Category?>) {

        popup = ListPopupWindow(context, null, 0, R.style.CategoryButtonPopup).also {

            it.setContentWidth(ListPopupWindow.WRAP_CONTENT)
            it.width = ListPopupWindow.WRAP_CONTENT
            it.anchorView = this

            val adapter = PopupAdapter()
            adapter.categories = categories
            adapter.handler = object : PopupAdapter.Handler {

                override fun onCategoryClicked(category: Category?) {

                    onPopupCategoryClicked(category)
                }
            }
            it.setAdapter(adapter)

            it.show()
        }
    }

    private fun onPopupCategoryClicked(category: Category?) {

        onCategorySelectedListener?.onCategorySelected(category)
        popup?.dismiss()
    }

    interface OnCategorySelectedListener {

        fun onCategorySelected(newCategory: Category?)
    }

    class PopupAdapter : BaseAdapter() {

        var categories = listOf<Category?>()

        lateinit var handler: Handler

        override fun getCount(): Int {

            return categories.size + 1
        }

        override fun getItem(position: Int): Any {

            return Unit
        }

        override fun getItemId(position: Int): Long {

            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val holder = if (convertView == null) {

                val holder = Holder(CategoryButtonRowBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
                holder.binding.root.tag = holder
                holder
            } else {

                convertView.tag as Holder
            }

            val category = if (position == 0) null else categories[position - 1]
            holder.bind(category, handler)

            return holder.binding.root
        }

        class Holder(val binding: CategoryButtonRowBinding) {

            fun bind(category: Category?, handler: Handler) {

                binding.categoryButtonRowName.textAlignment = TEXT_ALIGNMENT_INHERIT
                binding.category = category
                binding.handler = handler
                binding.executePendingBindings()
            }
        }

        interface Handler {

            fun onCategoryClicked(category: Category?)
        }
    }
}