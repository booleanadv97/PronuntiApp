package com.example.pronuntiapp.activities

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.common_utils.models.ConnectivityLiveData
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.ActivityParentBinding
import com.example.pronuntiapp.fragments.AppointmentsFragment
import com.example.pronuntiapp.fragments.AssignmentsFragment
import com.example.pronuntiapp.fragments.HomeFragment
import com.google.android.material.snackbar.Snackbar

class ParentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityParentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
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
        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.appointments -> replaceFragment(AppointmentsFragment())
                R.id.assignments -> replaceFragment(AssignmentsFragment())
                else -> {}
            }
            true
        }
    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment)
        fragmentTransaction.commit()
    }
}