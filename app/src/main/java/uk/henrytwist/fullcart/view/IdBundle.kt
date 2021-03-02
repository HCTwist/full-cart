package uk.henrytwist.fullcart.view

import android.os.Bundle
import androidx.appcompat.widget.DialogTitle

object IdBundle {

    private const val ID = "id"

    fun pack(id: Int) = Bundle().apply {

        putInt(ID, id)
    }

    fun unpack(bundle: Bundle) = bundle.getInt(ID)
}