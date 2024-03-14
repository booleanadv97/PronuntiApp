package com.example.pronuntiapp

import User
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database


abstract class Signup : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.registerButtonR)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailAddressR)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordR)
        val cognomeEditText = findViewById<EditText>(R.id.editTextCognome)
        val nomeEditText = findViewById<EditText>(R.id.editTextNome)
        signUpButton.setOnClickListener { _: View? ->
            val email: String = emailEditText.getText().toString().trim()
            val password: String = passwordEditText.getText().toString().trim()
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    task: Task<AuthResult?> ->
                    if (task.isSuccessful
                        && (cognomeEditText.toString()).all { it.isLetter() }
                        && (nomeEditText.toString()).all { it.isLetter() }
                        && (cognomeEditText.toString()).length >= 3
                        && (nomeEditText.toString()).length >= 3) {
                        // Sign up success, update UI with the signed-in user's information
                        Toast.makeText(
                            this@Signup, "Sign up successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        addUserToFirebase(email, password, cognomeEditText.toString(), nomeEditText.toString())
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
    private fun addUserToFirebase(email : String, password: String, cognome : String, nome : String){
        val auth: FirebaseAuth = Firebase.auth
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    user = User(currentUser?.uid.toString(), cognome, nome)
                    val database = Firebase.database
                    val users = database.getReference("users")
                    users.child("users").child(currentUser?.uid.toString()).setValue(user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

}