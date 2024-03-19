package com.example.pronuntiapptherapist.ui

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.ActivityTherapistMainBinding
import com.example.pronuntiapptherapist.fragments.HomeFragment
import com.example.pronuntiapptherapist.fragments.ManageExercisesFragment
import com.example.pronuntiapptherapist.fragments.ManageParentsFragment
import com.example.pronuntiapptherapist.models.ConnectivityLiveData
import com.google.android.material.snackbar.Snackbar

val fragmentsTitle = arrayOf(
    "Home",
    "Manage parents",
    "Manage exercises",
)
class TherapistMainActivity : AppCompatActivity() {
    lateinit var binding : ActivityTherapistMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = ActivityTherapistMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.parents -> replaceFragment(ManageParentsFragment())
                R.id.exercises -> replaceFragment(ManageExercisesFragment())
                else -> {}
            }
            true
        }
    }
     private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutTherapist, fragment)
        fragmentTransaction.commit()
    }
}