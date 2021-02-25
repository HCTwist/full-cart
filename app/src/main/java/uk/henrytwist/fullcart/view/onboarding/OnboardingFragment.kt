package uk.henrytwist.fullcart.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.OnboardingFragmentBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var binding: OnboardingFragmentBinding

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = OnboardingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        binding.onboardingPager.adapter = OnboardingAdapter(viewModel.contentPages, viewModel.end, viewModel)

        viewModel.currentPage.observe(viewLifecycleOwner) {

            binding.onboardingPager.currentItem = it
        }

        binding.onboardingPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {

                viewModel.onPageChanged(position)
            }
        })
    }
}