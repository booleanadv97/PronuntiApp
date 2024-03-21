package com.example.pronuntiapptherapist.models.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ManageAudioExerciseViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val audioExercisesRef = database.getReference("Audio Exercises")
    var _exerciseList : MutableLiveData<List<AudioExercise>> = MutableLiveData(emptyList())
    val exerciseList : LiveData<List<AudioExercise>> = _exerciseList
    fun getExerciseList(){
        audioExercisesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AudioExercise>()
                if(snapshot.exists()) {
                    for (child in snapshot.children) {
                        currentList = currentList + (
                                AudioExercise(
                                    child.getValue<AudioExercise>()?.exerciseName,
                                    child.getValue<AudioExercise>()?.exerciseDescription,
                                    child.getValue<AudioExercise>()?.audioUrl,
                                    child.getValue<AudioExercise>()?.audioId
                                )
                                )
                        _exerciseList.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}