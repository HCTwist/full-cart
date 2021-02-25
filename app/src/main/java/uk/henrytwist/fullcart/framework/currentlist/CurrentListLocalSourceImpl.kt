package uk.henrytwist.fullcart.framework.currentlist

import android.content.SharedPreferences
import uk.henrytwist.fullcart.data.currentlist.CurrentListLocalSource
import uk.henrytwist.fullcart.framework.SharedPreferencesModule
import javax.inject.Inject

class CurrentListLocalSourceImpl @Inject constructor(@SharedPreferencesModule.Data private val dataPreferences: SharedPreferences) : CurrentListLocalSource {

    override suspend fun get(): Int? {

        val id = dataPreferences.getInt(KEY, -1)
        return if (id == -1) null else id
    }

    override suspend fun set(listId: Int?) {

        if (listId == null) {

            dataPreferences.edit().remove(KEY).apply()
        } else {

            dataPreferences.edit().putInt(KEY, listId).apply()
        }
    }

    companion object {

        const val KEY = "current_screen"
    }
}