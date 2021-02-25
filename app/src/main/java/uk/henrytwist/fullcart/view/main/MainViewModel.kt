package uk.henrytwist.fullcart.view.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.usecases.CleanupCheckedItems
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cleanupCheckedItems: CleanupCheckedItems, private val onboardingRepository: OnboardingRepository) : NavigatorViewModel() {

    val firstTime = onboardingRepository.isFirstLaunch()

    init {

        viewModelScope.launch {

            cleanupCheckedItems()
        }
    }
}