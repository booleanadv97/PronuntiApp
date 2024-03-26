package com.example.pronuntiapp.models.patient

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.Answer
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.ImageExercise
import com.example.common_utils.models.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import java.time.Instant
import java.util.Calendar
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class PlayImgExViewModel : ViewModel() {
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val assignedExercisesRef = database.getReference("Assigned Exercises")
    private val imageExRef = database.getReference("Image Exercises")
    private val answersRef = database.getReference("Image Exercises Answers")
    private val usersRef = database.getReference("users")
    lateinit var outputMP4File: String
    private var audioUrl: String? = ""
    lateinit var mp4Uri: Uri
    private val STRING_LENGTH = 32
    val ADD_ANSWER_RESULT_OK = "OK"
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    var audioAnsId: String? = ""
    private var _imageExList: MutableLiveData<List<AssignedExercise>> = MutableLiveData(emptyList())
    val imageExList: LiveData<List<AssignedExercise>> = _imageExList
    private var _currentImgEx = MutableLiveData<ImageExercise>()
    val currentImgEx: LiveData<ImageExercise> = _currentImgEx
    private val storageRef = Firebase.storage.reference
    private val _addAnswerResult = MutableLiveData<String>()
    val addAnswerResult: LiveData<String> = _addAnswerResult
    private val _rewardType = MutableLiveData<String>()
    val rewardType: LiveData<String> = _rewardType
    private val _reward = MutableLiveData<String>()
    val reward: LiveData<String> = _reward
    fun resetAddAnswerResult() {
        _addAnswerResult.value = ""
    }

    fun getRewards() {
        assignedExercisesRef.child(_imageExList.value?.first()?.assignId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _rewardType.value = snapshot.getValue<AssignedExercise>()?.rewardType!!
                    _reward.value = snapshot.getValue<AssignedExercise>()?.reward!!
                    _imageExList.value = _imageExList.value!!.drop(1)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun givePoints(userId: String) {
        usersRef.child("userId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val points = snapshot.getValue<User>()?.points
                        val newPoints = points!! + 2
                        usersRef.child(userId).child("points").setValue(newPoints)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getImageEx() {
        imageExRef.child(_imageExList.value?.first()?.exerciseName!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _currentImgEx.value = snapshot.getValue<ImageExercise>()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

    fun addExerciseOnMP4Upload() {
        audioAnsId = randomString()
        storageRef.child("answers/ImageExercises/$audioAnsId").putFile(mp4Uri)
            .addOnSuccessListener {
                val audioRef = storageRef.child("answers/ImageExercises/$audioAnsId")
                val downloadUrlTask = audioRef.getDownloadUrl()
                downloadUrlTask.addOnSuccessListener { uri ->
                    audioUrl = uri.toString()
                    addImageExAnswer(_imageExList.value?.first()?.assignId!!)
                }
            }
    }

    private fun addImageExAnswer(assignId: String) {
        val answer = Answer(audioAnsId, audioUrl, assignId, Instant.now().toEpochMilli())
        answersRef.child(audioAnsId!!).setValue(answer)
            .addOnSuccessListener {
                _addAnswerResult.value = ADD_ANSWER_RESULT_OK
            }
            .addOnFailureListener {
                _addAnswerResult.value = "Errore nell'aggiunta della risposta!"
                Log.d("$this", it.stackTraceToString())
            }
    }

    fun isDateWithinRange(dateToCheck: Long, startDate: Long, endDate: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateToCheck
        val checkDate = calendar.get(Calendar.DAY_OF_MONTH)
        val checkMonth = calendar.get(Calendar.MONTH)
        val checkYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = startDate
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startMonth = calendar.get(Calendar.MONTH)
        val startYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = endDate
        val endDay = calendar.get(Calendar.DAY_OF_MONTH)
        val endMonth = calendar.get(Calendar.MONTH)
        val endYear = calendar.get(Calendar.YEAR)

        return (checkYear > startYear || (checkYear == startYear && checkMonth > startMonth) ||
                (checkYear == startYear && checkMonth == startMonth && checkDate >= startDay)) &&
                (checkYear < endYear || (checkYear == endYear && checkMonth < endMonth) ||
                        (checkYear == endYear && checkMonth == endMonth && checkDate < endDay))
    }

    fun checkIsSameDay(todayInMillis: Long, dateInTimeMillis: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = todayInMillis
        val tDay = calendar.get(Calendar.DAY_OF_MONTH)
        val tMonth = calendar.get(Calendar.MONTH)
        val tYear = calendar.get(Calendar.YEAR)
        calendar.timeInMillis = dateInTimeMillis
        val dDay = calendar.get(Calendar.DAY_OF_MONTH)
        val dMonth = calendar.get(Calendar.MONTH)
        val dYear = calendar.get(Calendar.YEAR)
        return (tDay == dDay && tMonth == dMonth && tYear == dYear)
    }

    fun getAssignedImageEx(userId: String) {
        assignedExercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AssignedExercise>()
                if (snapshot.exists()) {
                    for (assignedExercise in snapshot.children) {
                        val exercise = assignedExercise.getValue<AssignedExercise>()!!
                        if (exercise.userId == userId && exercise.exerciseType == "Image Exercise") {
                            val todayInMillis = Instant.now().toEpochMilli()
                            if (isDateWithinRange(
                                    todayInMillis,
                                    exercise.startDate!!,
                                    exercise.endDate!!
                                )
                            ) {
                                answersRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(answersSnapshot: DataSnapshot) {
                                        if (answersSnapshot.exists()) {
                                            var answeredToday = false
                                            for (child in answersSnapshot.children) {
                                                val answer = child.getValue<Answer>()
                                                val checkIsSameDay = checkIsSameDay(todayInMillis, answer?.ansDate!!)
                                                if (exercise.assignId.contentEquals(answer.assignId) && checkIsSameDay) {
                                                    answeredToday = true
                                                }
                                            }
                                            if (!answeredToday) {
                                                currentList =
                                                    currentList + exercise
                                                _imageExList.postValue(currentList)
                                            }
                                        } else {
                                            currentList =
                                                currentList + exercise
                                            _imageExList.postValue(currentList)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}