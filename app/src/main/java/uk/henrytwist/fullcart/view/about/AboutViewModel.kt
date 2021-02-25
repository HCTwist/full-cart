package uk.henrytwist.fullcart.view.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.henrytwist.androidbasics.Trigger
import uk.henrytwist.androidbasics.navigation.NavigationCommand
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.view.components.HeaderAdapter
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor() : NavigatorViewModel(), HeaderAdapter.Handler {

    fun getVersionNumber(context: Context): String {

        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        return info.versionName
    }

    fun onWebsiteClicked() {

        navigateToWesbite("https://henrytwist.uk")
    }

    fun onIcons8AttributionClicked() {

        navigateToWesbite("https://icons8.com")
    }

    private fun navigateToWesbite(url: String) {

        navigate(NavigationCommand.ActivityIntent(Intent(Intent.ACTION_VIEW, Uri.parse(url))))
    }

    override fun onClickBack() {

        navigateBack()
    }
}