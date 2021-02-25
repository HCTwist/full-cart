package uk.henrytwist.fullcart.view

import android.os.Bundle

object IdBundle {

    private const val ID = "id"

    fun pack(id: Int) = Bundle().apply {

        putInt(ID, id)
    }

    fun unpack(bundle: Bundle) = bundle.getInt(ID)
}