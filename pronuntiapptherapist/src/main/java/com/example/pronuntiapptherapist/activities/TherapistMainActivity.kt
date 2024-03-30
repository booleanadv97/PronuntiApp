package com.example.pronuntiapptherapist.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.common_utils.models.ConnectivityLiveData
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.databinding.ActivityTherapistMainBinding
import com.example.pronuntiapptherapist.fragments.HomeFragment
import com.example.pronuntiapptherapist.fragments.ManageExercisesFragment
import com.example.pronuntiapptherapist.fragments.ManageParentsFragment
import com.example.pronuntiapptherapist.fragments.appointment.AppointmentsFragment
import com.google.android.material.snackbar.Snackbar


class TherapistMainActivity : AppCompatActivity() {
    lateinit var binding : ActivityTherapistMainBinding
    override fun onStart() {
        super.onStart()

        val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            results ->
            for ((permission, granted) in results) {
                if (granted) {
                    // Permission granted
                } else {
                   Toast.makeText(this, "$permission${resources.getString(R.string.permission_not_granted)}",Toast.LENGTH_LONG).show()
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(arrayOf(RECORD_AUDIO, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(RECORD_AUDIO, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
        } else {
            requestPermissions.launch(arrayOf(RECORD_AUDIO, READ_EXTERNAL_STORAGE))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapistMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutTherapist, HomeFragment())
            fragmentTransaction.commit()
        }
        binding.bottomNavigationView.setOnItemSelectedListener{
            if(supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.parents -> replaceFragment(ManageParentsFragment())
                R.id.exercises -> replaceFragment(ManageExercisesFragment())
                R.id.appointments -> replaceFragment(AppointmentsFragment())
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