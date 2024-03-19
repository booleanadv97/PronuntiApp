package com.example.pronuntiapptherapist.models.AudioExercise

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class AddAudioExerciseViewModel : ViewModel() {
    private val STRING_LENGTH = 32
    val EXERCISE_RESULT_ONGOING = "ON"
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    var exerciseName : String? = ""
    var exerciseDescription : String? = ""
    private var audioUrl: String? = ""
    lateinit var outputMP4File: String
    lateinit var mp4Uri: Uri
    var audioAnsId: String? = ""
    private val storageRef = Firebase.storage.reference
    private val _progressBarLevel = MutableLiveData<Int>()
    val progressBarLevel : LiveData<Int> = _progressBarLevel
    private val _addExerciseResult = MutableLiveData<String>()
    val addExerciseResult : LiveData<String> = _addExerciseResult
    private val exercisesRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Audio Exercises"
        )
    fun addExerciseToRTDB(name: String, description: String) {
        _progressBarLevel.value = 0
        exercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.hasChild(name)){
                    _addExerciseResult.value = EXERCISE_RESULT_ONGOING
                    exerciseName = name
                    exerciseDescription = description
                    addExerciseOnMP4Upload()
                }else{
                    _addExerciseResult.value = "Esercizio già presente nel sistema"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                _addExerciseResult.value = "Error checking exercise: $error"
                Log.w("FirebaseUtil", "Error checking exercise: $error")
            }
        })
    }
    private fun addExerciseOnMP4Upload(){
        audioAnsId = randomString()
        storageRef.child("audioExercises/$audioAnsId").putFile(mp4Uri).addOnSuccessListener {
            val audioRef = storageRef.child("audioExercises/$audioAnsId")
            val downloadUrlTask = audioRef.getDownloadUrl()
            downloadUrlTask.addOnSuccessListener { uri ->
                audioUrl = uri.toString()
                _progressBarLevel.value = 75
                addExercise()
            }
        }
    }
    private fun addExercise(){
        val newExercise = AudioExercise(
            exerciseName,
            exerciseDescription,
            audioUrl,
            audioAnsId)
        exercisesRef.child(newExercise.exerciseName.toString()).setValue(newExercise)
            .addOnSuccessListener {
                _progressBarLevel.value = 100
                Log.d("$this", "Added new exercise ${newExercise.exerciseName}")
            }.addOnFailureListener {
                _addExerciseResult.value = "Failed to add exercise ${newExercise.exerciseName} $it"
                Log.d(
                    "$this",
                    "Failed to add exercise ${newExercise.exerciseName}, $it"
                )
            }
    }
    fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}