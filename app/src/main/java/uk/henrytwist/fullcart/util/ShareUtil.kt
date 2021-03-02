package uk.henrytwist.fullcart.util

import android.content.Intent

object ShareUtil {

    fun buildShareIntent(text: String): Intent {

        val shareIntent = Intent(Intent.ACTION_SEND).apply {

            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        return Intent.createChooser(shareIntent, null)
    }
}