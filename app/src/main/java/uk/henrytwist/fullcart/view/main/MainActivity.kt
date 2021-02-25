package uk.henrytwist.fullcart.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.framework.settings.SettingsRepositoryAndroid
import uk.henrytwist.fullcart.util.AlarmUtil
import uk.henrytwist.fullcart.util.NotificationUtil
import uk.henrytwist.fullcart.view.IdBundle
import uk.henrytwist.fullcart.view.hideSoftKeyboard

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), NavController.OnDestinationChangedListener {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(SettingsRepositoryAndroid.getDarkMode(resources, PreferenceManager.getDefaultSharedPreferences(this)))

        super.onCreate(savedInstanceState)

        NotificationUtil.registerNotificationChannels(this)
        AlarmUtil.scheduleUseByAlarm(this)

        viewModel

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.main_nav_graph)

        if (viewModel.firstTime) {

            graph.startDestination = R.id.onboardingFragment
            navController.graph = graph
        } else {

            graph.startDestination = R.id.listContainerFragment
            navController.setGraph(graph, getNotificationListIdArgs(intent))
        }
    }

    override fun onNewIntent(intent: Intent?) {

        super.onNewIntent(intent)

        if (intent != null) {

            getNotificationListIdArgs(intent)?.let {

                findNavController().popBackStack(R.id.listContainerFragment, false)
                findNavController().navigate(R.id.action_listContainerFragment_self, it)
            }
        }
    }

    private fun getNotificationListIdArgs(intent: Intent): Bundle? {

        return if (intent.extras?.containsKey(FROM_NOTIFICATION_LIST_ID) == true) {

            IdBundle.pack(intent.extras!!.getInt(FROM_NOTIFICATION_LIST_ID))
        } else {

            null
        }
    }

    override fun onResume() {
        super.onResume()

        findNavController().addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        super.onPause()

        findNavController().removeOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

        currentFocus?.hideSoftKeyboard()
    }

    private fun findNavController() = findNavController(R.id.nav_host_fragment)

    companion object {

        const val FROM_NOTIFICATION_LIST_ID = "from_notification_list_id"
    }
}