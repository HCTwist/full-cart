package uk.henrytwist.fullcart.view.pantryitem

import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import uk.henrytwist.androidbasics.livedata.observeEvent

object UseByDatePickerHelper {

    fun setup(fragment: Fragment, pantryViewModel: PantryItemViewModel) {

        pantryViewModel.pickUseByDate.observeEvent(fragment.viewLifecycleOwner) {

            val builder = MaterialDatePicker.Builder.datePicker()
            val constraints = CalendarConstraints.Builder()

            val today = MaterialDatePicker.todayInUtcMilliseconds()

            constraints.setStart(today)
            constraints.setOpenAt(today)
            constraints.setValidator(DateValidatorPointForward.from(today))

            pantryViewModel.useByDate.value?.let {

                builder.setSelection(it.toEpochMillis())
            }

            builder.setCalendarConstraints(constraints.build())

            val picker = builder.build()

            picker.addOnPositiveButtonClickListener {

                pantryViewModel.onUseByDatePicked(it)
            }

            picker.show(fragment.childFragmentManager, null)
        }
    }
}