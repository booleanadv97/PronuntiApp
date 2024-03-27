package com.example.pronuntiapp.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentHomeBinding
import com.example.pronuntiapp.models.parent.HomeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var txtUser : TextView
    private lateinit var txtAppointments : TextView
    private lateinit var txtAssignments : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObjects()
        binding.buttonLogout.setOnClickListener{
            Firebase.auth.signOut()
            requireActivity().finish()
        }
        val userId = Firebase.auth.currentUser?.uid!!
        viewModel.userId = userId
        viewModel.getUserData()
        viewModel.user.observe(viewLifecycleOwner){
            user ->
            val txtUserTxt = "${resources.getString(R.string.txt_welcome_back)} ${user.firstName} ${user.lastName}!"
            txtUser.text = txtUserTxt
        }
        viewModel.getAppointmentsNumber()
        viewModel.appointmentsCounter.observe(viewLifecycleOwner){
                counter ->
            val txtCounter : String
            if(counter < 1){
                txtCounter = resources.getString(R.string.txt_no_appointments)
                txtAppointments.text = txtCounter
            }else {
                txtCounter = "$counter ${resources.getString(R.string.txt_scheduled_appointments)}"
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
                txtCounter = resources.getString(R.string.txt_no_assignments)
                txtAssignments.text = txtCounter
            }else {
                txtCounter = "$counter ${resources.getString(R.string.txt_scheduled_assignments)}"
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
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    private fun initObjects(){
        txtAppointments = binding.txtAppointments
        txtUser = binding.txtUser
        txtAssignments = binding.txtAssignedExercises
        txtAppointments.visibility = View.INVISIBLE
        txtAssignments.visibility = View.INVISIBLE
    }
}