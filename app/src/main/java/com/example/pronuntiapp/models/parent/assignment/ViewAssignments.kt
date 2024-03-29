package com.example.pronuntiapp.models.parent.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AssignedExercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ViewAssignments : ViewModel() {
    val database =
        FirebaseDatabase.getInstance()
    private val assignedExercisesRef = database.getReference("Assigned Exercises")
    private var _assignedExercisesList: MutableLiveData<List<AssignedExercise>> =
        MutableLiveData(emptyList())
    val assignedExercisesList: LiveData<List<AssignedExercise>> = _assignedExercisesList
    fun getAssignedExercises(parentId: String) {
        assignedExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AssignedExercise>()
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val pId = child.getValue<AssignedExercise>()?.userId!!
                        if (pId == parentId) {
                            val exercise = child.getValue<AssignedExercise>()
                            currentList = currentList + exercise!!
                            _assignedExercisesList.postValue(currentList)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}