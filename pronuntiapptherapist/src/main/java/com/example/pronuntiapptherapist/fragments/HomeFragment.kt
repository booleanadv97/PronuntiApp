package com.example.pronuntiapptherapist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.FragmentTherapistHomeBinding
import com.example.pronuntiapptherapist.models.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentTherapistHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var txtAppointments : TextView
    private lateinit var txtAssignments : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtAppointments = binding.txtAppointments
        txtAssignments = binding.txtAssignedExercises
        viewModel.getAppointmentsNumber()
        viewModel.appointmentsCounter.observe(viewLifecycleOwner){
                counter ->
            val txtCounter : String
            if(counter < 1){
                txtCounter = resources.getString(R.string.txt_no_appointments_scheduled)
                txtAppointments.text = txtCounter
            }else {
                txtCounter = "$counter ${resources.getString(R.string.txt_appointments_scheduled)}"
                txtAppointments.text = txtCounter
            }
            if(txtAppointments.visibility == View.INVISIBLE)
                txtAppointments.visibility = View.VISIBLE
        }
        viewModel.getAssignmentsNumber()
        viewModel.assignmentsCounter.observe(viewLifecycleOwner){
                counter ->
            val txtCounter : String
            if(counter < 1){
                txtCounter = resources.getString(R.string.txt_no_unverified_assignments)
                txtAssignments.text = txtCounter
            }else {
                txtCounter = "$counter ${resources.getString(R.string.txt_unverified_assignments)}"
                txtAssignments.text = txtCounter
            }
            if(txtAssignments.visibility == View.INVISIBLE)
                txtAssignments.visibility = View.VISIBLE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentTherapistHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }
}