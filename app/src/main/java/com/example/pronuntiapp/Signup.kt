package com.example.pronuntiapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class Signup : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.registerButtonR)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddressR)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordR)
        signUpButton.setOnClickListener { _: View? ->
            val email: String = emailEditText.getText().toString().trim()
            val password: String = passwordEditText.getText().toString().trim()
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        Toast.makeText(
                            this@Signup, "Sign up successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(
                            this@Signup, "Sign up failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}