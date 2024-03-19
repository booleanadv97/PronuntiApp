package com.example.pronuntiapptherapist.models.ImageRecognitionExercise

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

class AddImageRecognitionExerciseViewModel : ViewModel() {
    private val STRING_LENGTH = 32
    val EXERCISE_RESULT_ONGOING = "ON"
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    lateinit var imageCorrectUri: Uri
    lateinit var imageAltUri: Uri
    private var correctImageId : String? = ""
    private var altImageId : String? = ""
    private var imgAltUrl : String? = ""
    private var imgCorrectUrl : String? = ""
    private val _txtProgress = MutableLiveData<String>()
    val txtProgress : LiveData<String> = _txtProgress
    var exerciseName : String? = ""
    var exerciseDescription : String? = ""
    var imgAltName: String? = ""
    var imgCorrectName: String? = ""
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
            "Image Recognition Exercises"
        )
    fun addExerciseToRTDB(name: String, description: String) {
        _progressBarLevel.value = 0
        _txtProgress.value = "${_progressBarLevel.value}%"
        exercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.hasChild(name)){
                        _addExerciseResult.value = EXERCISE_RESULT_ONGOING
                        exerciseName = name
                        exerciseDescription = description
                        exercisesRef.removeEventListener(this)
                        addExerciseOnImageCorrectUpload()
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
    fun addExerciseOnImageCorrectUpload(){
        correctImageId = randomString()
        storageRef.child("imageRecognitionExercises/$correctImageId").putFile(imageCorrectUri).addOnSuccessListener {
            storageRef.child("imageRecognitionExercises/$correctImageId").downloadUrl.addOnSuccessListener {
                imgCorrectUrl = it.toString()
                _progressBarLevel.value = 25
                _txtProgress.value = "${_progressBarLevel.value}%"
                addExerciseOnImageAltUpload()
            }
        }
    }
    private fun addExerciseOnImageAltUpload(){
        altImageId = randomString()
        storageRef.child("imageRecognitionExercises/$altImageId").putFile(imageAltUri).addOnSuccessListener {
            storageRef.child("imageRecognitionExercises/$altImageId").downloadUrl.addOnSuccessListener {
                imgAltUrl = it.toString()
                _progressBarLevel.value = 50
                _txtProgress.value = "${_progressBarLevel.value}%"
                addExerciseOnMP4Upload()
            }
        }
    }
    private fun addExerciseOnMP4Upload(){
        audioAnsId = randomString()
        storageRef.child("audioRecognitionExercises/$audioAnsId").putFile(mp4Uri).addOnSuccessListener {
            val audioRef = storageRef.child("audioRecognitionExercises/$audioAnsId")
            val downloadUrlTask = audioRef.getDownloadUrl()
            downloadUrlTask.addOnSuccessListener { uri ->
                audioUrl = uri.toString()
                _progressBarLevel.value = 75
                _txtProgress.value = "${_progressBarLevel.value}%"
                addExercise()
            }
        }
    }
    private fun addExercise(){
        val newExercise = ImageRecognitionExercise(
            exerciseName,
            exerciseDescription,
            imgCorrectUrl,
            correctImageId,
            imgAltUrl,
            altImageId,
            audioAnsId,
            audioUrl)
        exercisesRef.child(newExercise.exerciseName.toString()).setValue(newExercise)
            .addOnSuccessListener {
                _progressBarLevel.value = 100
                _txtProgress.value = "${_progressBarLevel.value}%"
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