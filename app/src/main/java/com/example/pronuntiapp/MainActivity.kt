package com.example.pronuntiapp
import TherapistActivities.TherapistMainActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
   override fun onStart() {
        super.onStart()
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, TherapistMainActivity::class.java))
            finish()
        }else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}