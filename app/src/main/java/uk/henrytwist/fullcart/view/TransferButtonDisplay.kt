package uk.henrytwist.fullcart.view

import android.content.Context

class TransferButtonDisplay(val show: Boolean, private val titleRes: Int, private val singleTitleRes: Int, private val singleName: String?) {

    fun resolveText(context: Context): String {

        return if (singleName == null) {

            context.getString(titleRes)
        } else {

            context.getString(singleTitleRes, singleName)
        }
    }
}