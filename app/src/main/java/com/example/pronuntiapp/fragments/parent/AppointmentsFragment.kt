package com.example.pronuntiapp.fragments.parent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.R
import com.example.pronuntiapp.adapters.AppointmentsListViewAdapter
import com.example.pronuntiapp.databinding.FragmentAppointmentsBinding
import com.example.pronuntiapp.models.parent.appointment.AppointmentsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class AppointmentsFragment : Fragment() {
    private lateinit var binding : FragmentAppointmentsBinding
    private lateinit var viewModel : AppointmentsViewModel
    private lateinit var listView : ListView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[AppointmentsViewModel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAuth = FirebaseAuth.getInstance()
        val parentId : String = mAuth.currentUser?.uid!!
        listView = binding.listViewAppointments
        viewModel.getAppointments(parentId)
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

        binding.buttonAddApointment.setOnClickListener{
            val c = Calendar.getInstance()
            val cyear = c.get(Calendar.YEAR)
            val cmonth = c.get(Calendar.MONTH)
            val cday = c.get(Calendar.DAY_OF_MONTH)
            val chourday = c.get(Calendar.HOUR_OF_DAY)
            val cminuteday = c.get(Calendar.MINUTE)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minuteOfDay ->
                        val aCalendar = Calendar.getInstance()
                        aCalendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfDay)
                        val date = aCalendar.timeInMillis
                        viewModel.addAppointment(parentId, date)
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
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater)
        return binding.root
    }
}