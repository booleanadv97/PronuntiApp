package com.example.pronuntiapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.ActivityPatientBinding
import com.example.pronuntiapp.fragments.patient.PatientHomeFragment

class PatientActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPatientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null){
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mainFrame, PatientHomeFragment())
            fragmentTransaction.commit()
        }
    }
}