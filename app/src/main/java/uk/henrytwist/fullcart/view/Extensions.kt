package uk.henrytwist.fullcart.view

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

fun View.hideSoftKeyboard() {

    getInputMethodManager()?.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.showSoftKeyboard() {

    // TODO Remove the need to delay
    postDelayed({

        requestFocus()
        getInputMethodManager()?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 50)
}

private fun View.getInputMethodManager() = context.getSystemService<InputMethodManager>()