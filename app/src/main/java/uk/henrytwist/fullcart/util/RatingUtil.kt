package uk.henrytwist.fullcart.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory

object RatingUtil {

    fun startRatingFlow(activity: Activity) {

        val manager = ReviewManagerFactory.create(activity)

        manager.requestReviewFlow().addOnCompleteListener {

            if (it.isSuccessful) {

                val info = it.result
                manager.launchReviewFlow(activity, info).addOnCompleteListener {

                    openPlayStoreListing(activity)
                }
            } else {

                openPlayStoreListing(activity)
            }
        }
    }

    fun openPlayStoreListing(context: Context) {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            setPackage("com.android.vending")
        }
        context.startActivity(intent)
    }
}