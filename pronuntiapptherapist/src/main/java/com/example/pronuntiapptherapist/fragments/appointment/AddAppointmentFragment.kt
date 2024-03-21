package com.example.pronuntiapptherapist.fragments.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.adapters.ManageParentsGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentAddAppointmentBinding
import com.example.pronuntiapptherapist.models.appointment.AppointmentsViewModel
import java.util.Calendar

@Suppress("DEPRECATION")
class AddAppointmentFragment : Fragment() {
    private lateinit var binding : FragmentAddAppointmentBinding
    private lateinit var gridView : GridView
    private lateinit var viewModel : AppointmentsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var parentId : String? = ""
        gridView = binding.gridViewParents
        viewModel.getParentsList()
        viewModel.parentsList.observe(viewLifecycleOwner) { list ->
            val manageParentsAdapter =
                context?.let { ManageParentsGridViewAdapter(parentsList = list, it) }
            gridView.adapter = manageParentsAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    parentId = list[position].userId!!
                    Toast.makeText(
                        context,
                        "Hai scelto il genitore ${list[position].firstName} ${list[position].lastName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        binding.buttonChooseDate.setOnClickListener {
            if (parentId?.isNotEmpty()!!) {
                val c = Calendar.getInstance()
                val cyear = c.get(Calendar.YEAR)
                val cmonth = c.get(Calendar.MONTH)
                val cday = c.get(Calendar.DAY_OF_MONTH)
                val chourday = c.get(Calendar.HOUR_OF_DAY)
                val cminuteday = c.get(Calendar.MINUTE)
                var hour: String
                var date: String
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        val timePicker =
                            TimePickerDialog(requireContext(), { _, hourOfDay, minuteOfDay ->
                                val strHour: String =
                                    if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                                val strMinute: String =
                                    if (minuteOfDay < 10) "0$minuteOfDay" else minuteOfDay.toString()
                                hour = "$strHour:$strMinute"
                                viewModel.addAppointment(parentId!!, date, hour)
                                viewModel.addAppointmentResult.observe(viewLifecycleOwner) {
                                    if (it.isNotEmpty()) {
                                        if (it == viewModel.APPOINTMENT_RESULT_OK) {
                                            Toast.makeText(
                                                context,
                                                "Appuntamento aggiunto con successo!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            fragmentManager?.popBackStack()
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
            }else{
                Toast.makeText(
                    context,
                    "Scegli il genitore per procedere",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAppointmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AppointmentsViewModel::class.java]
        return binding.root
    }
}