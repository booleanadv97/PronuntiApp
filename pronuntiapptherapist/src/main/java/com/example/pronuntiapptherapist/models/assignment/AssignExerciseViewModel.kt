package com.example.pronuntiapptherapist.models.assignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.UUID

class AssignExerciseViewModel : ViewModel() {
    val EXERCISE_RESULT_OK = "OK"
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val parentsRef = database.getReference("users")
    private val assignedExercisesRef = database.getReference("Assigned Exercises")
    var _parentsList: MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val parentsList: LiveData<List<User>> = _parentsList
    private val _exerciseResult = MutableLiveData<String>()
    val exerciseResult: LiveData<String> = _exerciseResult
    fun assignExercise(
        parentId: String,
        exerciseType: String,
        exerciseName: String,
        startDate: Long,
        endDate: Long,
        rewardType: String,
        reward: String
    ) {
        val assignedId = UUID.randomUUID().toString()
        val newAssign =
            AssignedExercise(
                assignedId,
                parentId,
                exerciseType,
                exerciseName,
                startDate,
                endDate,
                "",
                rewardType,
                reward
            )

        assignedExercisesRef.child(assignedId).setValue(newAssign)
            .addOnSuccessListener {
                _exerciseResult.value = EXERCISE_RESULT_OK
                Log.d("$this", "Created new assign $assignedId")
            }
            .addOnFailureListener {
                _exerciseResult.value = it.stackTraceToString()
                Log.d(
                    "$this",
                    "Failed to create new assign $assignedId, ${it.stackTraceToString()}"
                )
            }
    }

    fun resetResult() {
        _exerciseResult.value = ""
    }

    fun getParentsList() {
        parentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<User>()
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        currentList = currentList + child.getValue<User>()!!
                        _parentsList.postValue(currentList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}