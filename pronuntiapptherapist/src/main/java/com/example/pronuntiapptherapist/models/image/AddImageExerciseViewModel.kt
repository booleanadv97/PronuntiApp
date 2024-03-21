package com.example.pronuntiapptherapist.models.image

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

class AddImageExerciseViewModel : ViewModel() {
    val EXERCISE_STATUS_ONGOING = "ON"
    lateinit var imageUri: Uri
    var imgName: String? = ""
    private var imgUrl: String? = ""
    private var imageId : String? = ""
    var exerciseName : String? = ""
    var exerciseDescription : String? = ""
    private val _txtProgress = MutableLiveData<String>()
    val txtProgress : LiveData<String> = _txtProgress
    private val _progressBarLevel = MutableLiveData<Int>()
    val progressBarLevel : LiveData<Int> = _progressBarLevel
    private val imgExercisesRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Image Exercises"
        )
    private val STRING_LENGTH = 32
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val storageRef = Firebase.storage.reference
    private val _exerciseResult = MutableLiveData<String>()
    val exerciseResult : LiveData<String> = _exerciseResult
    private fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

    fun addImageExerciseToRTDB(exercise : String, description : String){
        _progressBarLevel.value = 0
        _txtProgress.value = "${_progressBarLevel.value}%"
        imgExercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(exercise)) {
                    _exerciseResult.value = EXERCISE_STATUS_ONGOING
                    _progressBarLevel.value = 25
                    _txtProgress.value = "${_progressBarLevel.value}%"
                    exerciseName = exercise
                    exerciseDescription = description
                    onCheckExerciseExists()
                }else{
                    _exerciseResult.value = "Esercizio già presente nel sistema"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _exerciseResult.value = "Errore: $error"
                Log.d("$this","Error: ${error.toException().stackTraceToString()}")
            }
        })
    }
    private fun onCheckExerciseExists() {
        imageId = randomString()
        val uTask = storageRef.child("imageExercises/$imageId").putFile(imageUri)
        uTask.addOnSuccessListener {
            storageRef.child("imageExercises")
                .child(imageId!!).downloadUrl.addOnSuccessListener { uri ->
                    imgUrl = uri.toString()
                    _progressBarLevel.value = 75
                    _txtProgress.value = "${_progressBarLevel.value}%"
                    onImgUpload()
                }
        }.addOnFailureListener {
            _exerciseResult.value = "Errore ${it.stackTraceToString()}"
        }
    }
    private fun onImgUpload(){
        imgExercisesRef.child(exerciseName!!)
            .setValue(
                ImageExercise(
                    exerciseName,
                    exerciseDescription,
                    imgUrl,
                    imageId
                )
            )
            .addOnSuccessListener {
                _progressBarLevel.value = 100
                _txtProgress.value = "${_progressBarLevel.value}%"
                Log.d(
                    "$this -> Save exercise to RTDB", "Exercise $exerciseName saved to RTDB"
                )
            }.addOnFailureListener {
                Log.d(
                    "$this -> Save exercise to RTDB", "Failed to save exercise : ${it.stackTraceToString()}"
                )
            }
    }
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