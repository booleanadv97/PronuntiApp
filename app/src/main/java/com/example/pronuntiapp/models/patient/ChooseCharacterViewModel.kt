package com.example.pronuntiapp.models.patient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlin.math.abs

class ChooseCharacterViewModel : ViewModel() {
    private val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val usersRef = database.getReference("users")
    private val _changeResult = MutableLiveData<String>()
    private val _patientPoints = MutableLiveData<Int>()
    val patientPoints: LiveData<Int> = _patientPoints
    val prices: HashMap<String, Int> =
        hashMapOf("hero_1" to 10, "hero_2" to 20, "hero_3" to 30)
    val changeResult: LiveData<String> = _changeResult
    val CHANGE_RESULT_OK = "OK"

    fun resetResult() {
        _changeResult.value = ""
    }

    private fun checkPrice(character: String, points: Int): Int {
        return points - prices[character]!!
    }

    fun getPatientPoints(userId: String) {
        usersRef.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _patientPoints.value = snapshot.child("points").getValue<Int>()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun changeCharacter(userId: String, character: String) {
        val remainingPoints = checkPrice(character, _patientPoints.value!!)
        if (remainingPoints >= 0) {
            usersRef.child(userId).child("character").setValue(character)
                .addOnSuccessListener {
                    _changeResult.value = CHANGE_RESULT_OK
                    Log.d("$this", "$userId changed his character")
                }
                .addOnFailureListener {
                    _changeResult.value = "Errore nel cambiamento del personaggio!"
                    Log.d(
                        "$this",
                        "$userId failed to change his character, ${it.stackTraceToString()}"
                    )
                }
        } else {
            _changeResult.value = "Devi avere ${prices[character]} punti per scegliere questo personaggio! Ti servono altri ${abs(remainingPoints)}."
        }
    }
}