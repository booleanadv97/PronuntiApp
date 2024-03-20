package com.example.pronuntiapptherapist.fragments.Assignment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.adapters.ManageParentsGridViewAdapter
import com.example.pronuntiapptherapist.databinding.FragmentAssignExerciseBinding
import com.example.pronuntiapptherapist.models.Assignment.AssignExerciseViewModel
import java.util.Calendar

@Suppress("DEPRECATION")
class AssignExercise : Fragment() {
    private lateinit var binding : FragmentAssignExerciseBinding
    private lateinit var viewModel : AssignExerciseViewModel
    private lateinit var gridView : GridView
    private var startDate : String ? =""
    private var endDate : String ? =""
    private var parentId : String? =""
    private var exerciseName : String? =""
    private var exerciseType : String? =""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseName = arguments?.getString("exerciseName")
        exerciseType = arguments?.getString("exerciseType")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dateStart = Calendar.getInstance()
        val dateEnd = Calendar.getInstance()
        gridView = binding.gridViewParents
        viewModel.getParentsList()
        viewModel.parentsList.observe(viewLifecycleOwner) {
                list ->
            val manageParentsAdapter =
                context?.let { ManageParentsGridViewAdapter(parentsList = list, it) }
            gridView.adapter = manageParentsAdapter
            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    parentId = list[position].userId
                    Toast.makeText(context, "Hai scelto il genitore ${list[position].firstName} ${list[position].lastName}",
                        Toast.LENGTH_SHORT).show()
                }
        }
        binding.buttonStartDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    startDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateStart.set(year, monthOfYear, dayOfMonth)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.setMinDate(c.timeInMillis)
            datePickerDialog.show()
        }
        binding.buttonEndDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    endDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEnd.set(year, monthOfYear, dayOfMonth)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.setMinDate(c.timeInMillis)
            datePickerDialog.show()
        }
        binding.buttonSubmit.setOnClickListener{
            if(checkFields()){
                if(dateStart.compareTo(dateEnd) < 0) {
                    viewModel.assignExercise(
                        parentId!!,
                        exerciseType!!,
                        exerciseName!!,
                        startDate!!,
                        endDate!!
                    )
                    viewModel.exerciseResult.observe(viewLifecycleOwner) {
                        if (it == viewModel.EXERCISE_RESULT_OK) {
                            Toast.makeText(
                                context, "Esercizio assegnato con successo",
                                Toast.LENGTH_SHORT
                            ).show()
                            fragmentManager?.popBackStack()
                        } else {
                            Toast.makeText(
                                context, "Errore: $it",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }else{
                    Toast.makeText(
                        context, requireActivity().resources.getString(R.string.wrong_date),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    context, "Compila tutti i campi richiesti",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun checkFields() : Boolean {
        return (parentId!!.isNotEmpty() && startDate!!.isNotEmpty()
            && endDate!!.isNotEmpty() && exerciseName!!.isNotEmpty() && exerciseType!!.isNotEmpty())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignExerciseBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AssignExerciseViewModel::class.java]
        return binding.root
    }
}