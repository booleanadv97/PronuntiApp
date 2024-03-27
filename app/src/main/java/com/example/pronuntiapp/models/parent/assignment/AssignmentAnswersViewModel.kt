package com.example.pronuntiapp.models.parent.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AudioAnswer
import com.example.common_utils.models.ImageAnswer
import com.example.common_utils.models.ImageReconAnswer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class AssignmentAnswersViewModel : ViewModel() {
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val audioAnswersRef = database.getReference("Audio Exercises Answers")
    private val imageAnswersRef = database.getReference("Image Exercises Answers")
    private val imageReconAnswersRef = database.getReference("Image Recognition Exercises Answers")
    private val _imageExAnswers : MutableLiveData<List<ImageAnswer>> = MutableLiveData(emptyList())
    val imageAnswers : LiveData<List<ImageAnswer>> = _imageExAnswers
    private val _audioAnswers : MutableLiveData<List<AudioAnswer>> = MutableLiveData(emptyList())
    val audioAnswers : LiveData<List<AudioAnswer>> = _audioAnswers
    private val _imageReconAnswers : MutableLiveData<List<ImageReconAnswer>> = MutableLiveData(emptyList())
    val imageReconAnswers : LiveData<List<ImageReconAnswer>> = _imageReconAnswers
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