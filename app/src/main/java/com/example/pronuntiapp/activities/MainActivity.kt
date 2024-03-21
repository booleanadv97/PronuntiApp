package com.example.pronuntiapp.activities
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pronuntiapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.buttonContinueAsParent.setOnClickListener{
                startActivity(Intent(this, ParentActivity::class.java))
            }
            binding.buttonContinueAsPatient.setOnClickListener{
                startActivity(Intent(this, PatientActivity::class.java))
            }
        }else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}