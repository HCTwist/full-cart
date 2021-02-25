package uk.henrytwist.fullcart.view.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.usecases.SetupExperience
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
        private val setupExperience: SetupExperience
) : NavigatorViewModel(), OnboardingAdapter.EndHandler {

    val contentPages = listOf(
            OnboardingPage(R.string.onboarding_shopping_title, R.drawable.hare_and_tortoise, R.string.onboarding_shopping_description),
            OnboardingPage(R.string.onboarding_pantry_title, R.drawable.raccoon, R.string.onboarding_pantry_description),
            OnboardingPage(R.string.onboarding_notifications_title, R.drawable.deer, R.string.onboarding_notifications_description)
    )

    private var addShoppingList = true
    private var addPantry = true
    private var addCategories = true

    val end: OnboardingEnd
        get() = OnboardingEnd(addShoppingList, addPantry, addCategories)

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int>
        get() = _currentPage

    fun onClickNext() {

        if (currentPage.value == contentPages.size) {

            finish()
        } else {

            _currentPage.value = currentPage.value!! + 1
        }
    }

    fun onClickSkip() {

        finish()
    }

    fun onPageChanged(page: Int) {

        _currentPage.value = page
    }

    override fun onAddShoppingListCheckChanged(checked: Boolean) {

        addShoppingList = checked
    }

    override fun onAddPantryCheckChanged(checked: Boolean) {

        addPantry = checked
    }

    override fun onAddCategoriesCheckChanged(checked: Boolean) {

        addCategories = checked
    }

    private fun finish() {

        viewModelScope.launch {

            setupExperience(addShoppingList, addPantry, addCategories)
            navigate(R.id.action_onboardingFragment_to_listContainerFragment)
        }
    }
}