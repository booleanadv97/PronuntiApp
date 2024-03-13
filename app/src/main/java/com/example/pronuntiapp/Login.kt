package com.example.pronuntiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        val logInButton = findViewById<Button>(R.id.loginButton)
        val signupLink = findViewById<TextView>(R.id.signupLink)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddressL)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordL)
        signupLink.setOnClickListener{
            startActivity(Intent(this, Signup::class.java));
        }
        logInButton.setOnClickListener { v: View? ->
            val email: String = emailEditText.getText().toString().trim()
            val password: String = passwordEditText.getText().toString().trim()
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Log in success, update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        Toast.makeText(
                            this@Login, "Log in successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If log in fails, display a message to the user.
                        Toast.makeText(
                            this@Login, "Log in failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}