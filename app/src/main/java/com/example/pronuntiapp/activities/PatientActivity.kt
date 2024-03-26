package com.example.pronuntiapp.activities

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.common_utils.models.ConnectivityLiveData
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.ActivityPatientBinding
import com.example.pronuntiapp.fragments.patient.PatientHomeFragment
import com.google.android.material.snackbar.Snackbar

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
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val connectivityLiveData = ConnectivityLiveData(this)
        connectivityLiveData.observe(this) { isConnected ->
            if (!isConnected) {
                val snackBarNoInternet = Snackbar.make(
                    findViewById(R.id.toolbar),
                    "No internet connection",
                    Snackbar.LENGTH_LONG
                )
                snackBarNoInternet.setAction("Settings") {
                    val wifiIntent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                    startActivity(wifiIntent)
                }
                snackBarNoInternet.show()
            }
        }
    }
}