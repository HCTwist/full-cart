package uk.henrytwist.fullcart.view

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uk.henrytwist.androidbasics.Trigger
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.fullcart.models.ListMetaSummary

class ListSelectionDialogHelper(private val handler: Handler, private val titleRes: Int) {

    private lateinit var items: List<Item>

    private val showSelectionDialog = MutableLiveData<Trigger>()

    fun getSingleNameIfExists(): String? {

        return if (items.size == 1) items[0].name else null
    }

    fun onSelectListRequest() {

        when (items.size) {

            0 -> {
            }
            1 -> handler.onListSelected(items[0].id)
            else -> showSelectionDialog.trigger()
        }
    }

    fun isNotEmpty() = items.isNotEmpty()

    fun observeShowSelectionDialog(context: Context, lifecycleOwner: LifecycleOwner) {

        showSelectionDialog.observe(lifecycleOwner) {

            MaterialAlertDialogBuilder(context).setTitle(titleRes).setItems(items.map { it.name }.toTypedArray()) { _, which ->

                handler.onListSelected(items[which].id)
            }.show()
        }
    }

    fun setLists(meta: List<ListMetaSummary>) {

        items = meta.map {

            Item(it.id, it.name)
        }
    }

    class Item(val id: Int, val name: String)

    fun interface Handler {

        fun onListSelected(listId: Int)
    }
}