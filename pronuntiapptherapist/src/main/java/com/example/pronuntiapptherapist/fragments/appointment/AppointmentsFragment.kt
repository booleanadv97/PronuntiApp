package com.example.pronuntiapptherapist.fragments.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.AppointmentsListViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentAppointmentsBinding
import com.example.pronuntiapptherapist.models.appointment.AppointmentsViewModel

class AppointmentsFragment : Fragment() {
    private lateinit var binding : FragmentAppointmentsBinding
    private lateinit var viewModel : AppointmentsViewModel
    private lateinit var listView : ListView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.listViewAppointments
        viewModel.getAppointments()
        viewModel.appointmentsList.observe(viewLifecycleOwner){
            list ->
            val manageParentsAdapter =
                context?.let { AppointmentsListViewAdapter(it, appointmentsList = list) }
            listView.adapter = manageParentsAdapter
            if(list.isEmpty())
                binding.textView.text = requireActivity().resources.getString(R.string.no_appointments)
            else
                binding.textView.text = requireActivity().resources.getString(R.string.appointments_land_txt)
        }

        /*binding.buttonAddApointment.setOnClickListener{
            val c = Calendar.getInstance()
            val cyear = c.get(Calendar.YEAR)
            val cmonth = c.get(Calendar.MONTH)
            val cday = c.get(Calendar.DAY_OF_MONTH)
            val chourday = c.get(Calendar.HOUR_OF_DAY)
            val cminuteday = c.get(Calendar.MINUTE)
            var hour : String
            var date : String
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minuteOfDay ->
                        val strHour : String = if(hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                        val strMinute : String = if(minuteOfDay < 10) "0$minuteOfDay" else minuteOfDay.toString()
                        hour = "$strHour:$strMinute"
                        viewModel.addAppointment(parentId, date, hour)
                        viewModel.addAppointmentResult.observe(viewLifecycleOwner){
                            if(it.isNotEmpty()) {
                                if (it == viewModel.APPOINTMENT_RESULT_OK) {
                                    Toast.makeText(
                                        context,
                                        "Appuntamento aggiunto con successo!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                                viewModel.resetResult()
                            }
                        }
                    }, chourday, cminuteday, true)
                    timePicker.show()
                },
                cyear,
                cmonth,
                cday
            )
            datePickerDialog.datePicker.minDate = c.timeInMillis
            datePickerDialog.show()
        }*/
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AppointmentsViewModel::class.java]
        return binding.root
    }
}