package com.example.pronuntiapptherapist.models.assignment

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
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
   private val assignedExercisesRef = database.getReference("Assigned Exercises")
    private var _assignedExercisesList : MutableLiveData<List<AssignedExercise>> = MutableLiveData(emptyList())
    val assignedExercisesList : LiveData<List<AssignedExercise>> = _assignedExercisesList
    fun getAssignedExercises(parentId : String){
        assignedExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AssignedExercise>()
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        val pId : String = child.child("userId").getValue<String>()!!
                        if(pId == parentId){
                            val esercizio = child.getValue<AssignedExercise>()
                            currentList = currentList + esercizio!!
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