package com.example.pronuntiapp.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.FragmentChooseCharacterBinding
import com.example.pronuntiapp.models.patient.ChooseCharacterViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class PatientChooseCharacterFragment : Fragment() {
    private lateinit var binding: FragmentChooseCharacterBinding
    private lateinit var viewModel: ChooseCharacterViewModel
    private lateinit var hero1: LottieAnimationView
    private lateinit var hero2: LottieAnimationView
    private lateinit var hero3: LottieAnimationView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUser: String = Firebase.auth.currentUser?.uid!!
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPatientPoints(currentUser)
        viewModel.patientPoints.observe(viewLifecycleOwner) { value ->
            val txtPoints =
                "${requireActivity().resources.getString(R.string.patient_points_txt)} :  ${value.toString()}"
            binding.patientPoints.text = txtPoints
        }
        binding.txtPriceHero1.text = viewModel.prices["hero_1"].toString()
        binding.txtPriceHero2.text = viewModel.prices["hero_2"].toString()
        binding.txtPriceHero3.text = viewModel.prices["hero_3"].toString()
        hero1 = binding.hero1
        hero2 = binding.hero2
        hero3 = binding.hero3
        hero1.setOnClickListener {
            viewModel.changeCharacter(currentUser, "hero_1")
            viewModel.changeResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    if (it == viewModel.CHANGE_RESULT_OK)
                        Toast.makeText(
                            context,
                            "Hai cambiato il tuo personaggio!",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.resetResult()
                }
            }
        }
        hero2.setOnClickListener {
            viewModel.changeCharacter(currentUser, "hero_2")
            viewModel.changeResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    if (it == viewModel.CHANGE_RESULT_OK)
                        Toast.makeText(
                            context,
                            "Hai cambiato il tuo personaggio!",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.resetResult()
                }
            }
        }
        hero3.setOnClickListener {
            viewModel.changeCharacter(currentUser, "hero_3")
            viewModel.changeResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    if (it == viewModel.CHANGE_RESULT_OK)
                        Toast.makeText(
                            context,
                            "Hai cambiato il tuo personaggio!",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.resetResult()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseCharacterBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ChooseCharacterViewModel::class.java]
        return binding.root
    }
}