package uk.henrytwist.fullcart.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class MonitorLiveData : MediatorLiveData<Boolean>() {

    init {

        value = false
    }

    fun monitor(vararg toMonitor: LiveData<*>) {

        toMonitor.forEach {

            var ignore = value != null
            addSource(it) {

                if (value != true) {

                    if (ignore) {

                        ignore = false
                    } else {

                        value = true
                    }
                }
            }
        }
    }
}