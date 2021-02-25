package uk.henrytwist.fullcart.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.databinding.SettingsContainerFragmentBinding
import uk.henrytwist.fullcart.view.components.HeaderAdapter

class SettingsContainerFragment : Fragment() {

    private lateinit var binding: SettingsContainerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = SettingsContainerFragmentBinding.inflate(inflater, container, false)
        binding.handler = object : HeaderAdapter.Handler {

            override fun onClickBack() {

                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) childFragmentManager.beginTransaction().replace(R.id.settings_container, SettingsFragment()).commit()
    }
}