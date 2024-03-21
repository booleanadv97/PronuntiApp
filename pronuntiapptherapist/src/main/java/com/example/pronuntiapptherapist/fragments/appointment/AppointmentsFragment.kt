package com.example.pronuntiapptherapist.fragments.appointment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
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
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                        if(list[position].therapistCheck == ""){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Conferma")
                            builder.setMessage("Vuoi confermare l'appuntamento?")
                            builder.setPositiveButton("Yes") { dialog, _ ->
                                viewModel.confirmAppointment(list[position])
                                viewModel.confirmAppointment.observe(viewLifecycleOwner){
                                    if(it.isNotEmpty()){
                                        if(it == viewModel.APPOINTMENT_RESULT_OK){
                                            Toast.makeText(context, "Stato appuntamento aggiornato!",Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                        }
                                        viewModel.resetConfirm()
                                    }
                                }
                                dialog.dismiss()
                            }
                            builder.setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                }
            if(list.isEmpty())
                binding.textView.text = requireActivity().resources.getString(R.string.no_appointments)
            else
                binding.textView.text = requireActivity().resources.getString(R.string.appointments_land_txt)
        }
        binding.buttonAddApointment.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutTherapist, AddAppointmentFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
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