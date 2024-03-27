package com.example.pronuntiapptherapist.models.assignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.AudioAnswer
import com.example.common_utils.models.ImageAnswer
import com.example.common_utils.models.ImageReconAnswer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Calendar

class AssignmentAnswersViewModel : ViewModel() {
    val THERAPIST_CHECK_OK = "Si"
    val MARK_AS_COMPLETE_OK = "Stato del compito cambiato con successo!"
    val THERAPIST_CHECK_FIELD = "therapistCheck"
    val ASSIGN_NOT_COMPLETED = "Il paziente non ha ancora completato il compito!"
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val assignmentsRef = database.getReference("Assigned Exercises")
    private val audioAnswersRef = database.getReference("Audio Exercises Answers")
    private val imageAnswersRef = database.getReference("Image Exercises Answers")
    private val imageReconAnswersRef = database.getReference("Image Recognition Exercises Answers")
    private val _imageExAnswers : MutableLiveData<List<ImageAnswer>> = MutableLiveData(emptyList())
    val imageAnswers : LiveData<List<ImageAnswer>> = _imageExAnswers
    private val _audioAnswers : MutableLiveData<List<AudioAnswer>> = MutableLiveData(emptyList())
    val audioAnswers : LiveData<List<AudioAnswer>> = _audioAnswers
    private val _imageReconAnswers : MutableLiveData<List<ImageReconAnswer>> = MutableLiveData(emptyList())
    val imageReconAnswers : LiveData<List<ImageReconAnswer>> = _imageReconAnswers
    private val _markResult = MutableLiveData<String>()
    val markResult : LiveData<String> = _markResult

    fun resetMarkResult(){
        _markResult.value = ""
    }
    fun markAssignAsComplete(assignId : String){
        assignmentsRef.child(assignId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val endDate = snapshot.getValue<AssignedExercise>()?.endDate
                val c = Calendar.getInstance()
                val currDate = c.timeInMillis
                if(currDate >= endDate!!) {
                    assignmentsRef.child(assignId).child(THERAPIST_CHECK_FIELD).setValue(THERAPIST_CHECK_OK)
                        .addOnSuccessListener {
                            _markResult.value = MARK_AS_COMPLETE_OK
                        }
                        .addOnFailureListener {
                            _markResult.value = "Errore, non è stato possibile cambiare lo stato del compito"
                            Log.d("$this", it.stackTraceToString())
                        }
                }
                else
                    _markResult.value = ASSIGN_NOT_COMPLETED
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun getAudioAssignAnswers(assignId : String){
        audioAnswersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AudioAnswer>()
                for(child in snapshot.children){
                    val childAssignId = child.getValue<AudioAnswer>()?.assignId
                    if(childAssignId == assignId){
                        currentList = currentList + child.getValue<AudioAnswer>()!!
                        _audioAnswers.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getImageAssignAnswers(assignId : String){
        imageAnswersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<ImageAnswer>()
                for(child in snapshot.children){
                    val childAssignId = child.getValue<ImageAnswer>()?.assignId
                    if(childAssignId == assignId){
                        currentList = currentList + child.getValue<ImageAnswer>()!!
                        _imageExAnswers.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getImageReconAssignAnswers(assignId : String){
        imageReconAnswersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<ImageReconAnswer>()
                for(child in snapshot.children){
                    val childAssignId = child.getValue<ImageReconAnswer>()?.assignId
                    if(childAssignId == assignId){
                        currentList = currentList + child.getValue<ImageReconAnswer>()!!
                        _imageReconAnswers.postValue(currentList)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}