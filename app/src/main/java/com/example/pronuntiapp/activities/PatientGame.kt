package com.example.pronuntiapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.ActivityPatientGameBinding
import com.example.pronuntiapp.fragments.patient.PatientChooseExercise

class PatientGame : AppCompatActivity() {
    private lateinit var binding: ActivityPatientGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPatientGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null)
            replaceFragment(PatientChooseExercise())
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framePatientGame, fragment)
        fragmentTransaction.commit()
    }
}