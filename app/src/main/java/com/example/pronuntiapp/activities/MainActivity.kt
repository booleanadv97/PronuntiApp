package com.example.pronuntiapp.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.common_utils.models.ConnectivityLiveData
import com.example.pronuntiapp.R
import com.example.pronuntiapp.databinding.ActivityMainBinding
import com.example.pronuntiapp.models.MainActivityViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonContinueAsParent.visibility = View.INVISIBLE
        binding.buttonContinueAsPatient.visibility = View.INVISIBLE
        val requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                for ((permission, granted) in results) {
                    if (granted) {
                        // Permission granted
                    } else {
                        Toast.makeText(
                            this,
                            "$permission${resources.getString(R.string.permission_not_granted)}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            requestPermissions.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
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
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        enablePath()
                    } else {
                        continueDialog()
                    }
                }
        }else{
            continueDialog()
        }
    }

    private fun continueDialog(){
        val mAuth = FirebaseAuth.getInstance()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.access_type))
        builder.setMessage(resources.getString(R.string.ask_access_type))
        builder.setPositiveButton(resources.getString(R.string.access_default_ok)) { _, _ ->
            mAuth.createUserWithEmailAndPassword(
                viewModel.defaultEmail,
                viewModel.defaultPassword
            )
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        initDefaultSetup(task)
                        mAuth.signInWithEmailAndPassword(
                            viewModel.defaultEmail,
                            viewModel.defaultPassword
                        )
                            .addOnCompleteListener(
                                this
                            ) { signInNewDefaultTask: Task<AuthResult?> ->
                                if (signInNewDefaultTask.isSuccessful) {
                                    enablePath()
                                }
                            }
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            mAuth.signInWithEmailAndPassword(
                                viewModel.defaultEmail,
                                viewModel.defaultPassword
                            )
                                .addOnCompleteListener(
                                    this
                                ) { alreadyExistsTask: Task<AuthResult?> ->
                                    if (alreadyExistsTask.isSuccessful) {
                                        enablePath()
                                    }
                                }
                        }
                    }
                }
        }
        builder.setNegativeButton(resources.getString(R.string.access_default_no)) { _, _ ->
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun enablePath(){
        binding.buttonContinueAsParent.visibility =
            View.VISIBLE
        binding.buttonContinueAsPatient.visibility =
            View.VISIBLE
        binding.buttonContinueAsParent.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ParentActivity::class.java
                )
            )
        }
        binding.buttonContinueAsPatient.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PatientActivity::class.java
                )
            )
        }
    }
    private fun initDefaultSetup(task : Task<AuthResult?>) {
        loadDefaultResources()
        setContentView(R.layout.fragment_wait_upload)
        viewModel.addUserToRTDB(task.result?.user?.uid.toString())
        viewModel.progressBarLevel.observe(this) { progressBarLevel ->
            val txtProgress =
                findViewById<TextView>(R.id.txtProgress)
            val progressBar =
                findViewById<ProgressBar>(R.id.progressBarUpload)
            progressBar.progress = progressBarLevel
            if (progressBarLevel < 100) {
                viewModel.txtProgress.observe(this) { text ->
                    txtProgress.text = text
                }
            } else {
                setContentView(binding.root)
            }
        }
    }
    private fun loadDefaultResources(){
        viewModel.audio_exercise_1_uri =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.audio_exercise_1)
        viewModel.audio_exercise_2_uri =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.audio_exercise_2)
        viewModel.audio_exercise_3_uri =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.audio_exercise_3)
        viewModel.image_exercise_1_uri =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_1)
        viewModel.image_exercise_2_uri =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_2)
        viewModel.image_recon_audio_1 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.image_recon_1_audio)
        viewModel.image_recon_audio_2 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.image_recon_2_audio)
        viewModel.image_recon_correct_img_1 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_2)
        viewModel.image_recon_alt_img_1 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_1)
        viewModel.image_recon_correct_img_2 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_1)
        viewModel.image_recon_alt_img_2 =
            Uri.parse("android.resource://" + this.packageName + "/" + R.raw.default_image_exercise_2)

    }
}