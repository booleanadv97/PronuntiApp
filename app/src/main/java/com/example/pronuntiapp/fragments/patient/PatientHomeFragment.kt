package com.example.pronuntiapp.fragments.patient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pronuntiapp.R
import com.example.pronuntiapp.activities.PatientGame
import com.example.pronuntiapp.databinding.FragmentPatientHomeBinding


class PatientHomeFragment : Fragment() {

    private lateinit var binding: FragmentPatientHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener{
            startActivity(Intent(requireActivity(), PatientGame::class.java))
        }
        binding.buttonChangeCharacter.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mainFrame, ChooseCharacterFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        binding.buttonTopList.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mainFrame, RankingsFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientHomeBinding.inflate(inflater)
        return binding.root
    }


}