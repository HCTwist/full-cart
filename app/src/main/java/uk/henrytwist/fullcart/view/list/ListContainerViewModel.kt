package uk.henrytwist.fullcart.view.list

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.event
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.usecases.FindStartingList
import uk.henrytwist.fullcart.usecases.GetListMeta
import uk.henrytwist.fullcart.view.IdBundle
import uk.henrytwist.kotlinbasics.Event
import javax.inject.Inject

@HiltViewModel
class ListContainerViewModel @Inject constructor(private val getListMeta: GetListMeta, private val findStartingList: FindStartingList) : ViewModel() {

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>>
        get() = _state

    fun args(args: Bundle?) {

        viewModelScope.launch {

            if (args == null) {

                resolveState()
            } else {

                val meta = getListMeta(IdBundle.unpack(args))
                if (meta == null) {

                    resolveState()
                } else {

                    _state.event = State(meta.type, args)
                }
            }
        }
    }

    private suspend fun resolveState() {

        val startScreen = findStartingList()

        val args = startScreen?.let { IdBundle.pack(it.id) }
        _state.event = State(startScreen?.type, args)
    }

    class State(val type: ListType?, val args: Bundle?)
}