package com.example.pronuntiapp.models.patient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.ImageRecognitionExercise
import com.example.common_utils.models.ImageReconAnswer
import com.example.common_utils.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.Instant
import java.util.Calendar
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class PlayImageReconExViewModel : ViewModel() {
    val database =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val assignedExercisesRef = database.getReference("Assigned Exercises")
    private val imageReconExRef = database.getReference("Image Recognition Exercises")
    private val answersRef = database.getReference("Image Recognition Exercises Answers")
    private val usersRef = database.getReference("users")
    var answerCorrect : String? = ""
    private val STRING_LENGTH = 32
    val ADD_ANSWER_RESULT_OK = "OK"
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var _imageReconExList: MutableLiveData<List<AssignedExercise>> = MutableLiveData(emptyList())
    val imageReconExList: LiveData<List<AssignedExercise>> = _imageReconExList
    private var _currentEx = MutableLiveData<ImageRecognitionExercise>()
    val currentEx: LiveData<ImageRecognitionExercise> = _currentEx
    private val _addAnswerResult = MutableLiveData<String>()
    val addAnswerResult: LiveData<String> = _addAnswerResult
    private val _rewardType = MutableLiveData<String>()
    val rewardType: LiveData<String> = _rewardType
    private val _reward = MutableLiveData<String>()
    val reward: LiveData<String> = _reward
    private var _userHero = MutableLiveData<String>()
    val userHero : LiveData<String> = _userHero

    fun getHero(userId : String){
        usersRef.child(userId).child("character").get().addOnSuccessListener {
            _userHero.value = it.value.toString()
        }
    }
    fun resetAddAnswerResult() {
        _addAnswerResult.value = ""
    }

    fun getRewards() {
        assignedExercisesRef.child(_imageReconExList.value?.first()?.assignId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _rewardType.value = snapshot.getValue<AssignedExercise>()?.rewardType!!
                    _reward.value = snapshot.getValue<AssignedExercise>()?.reward!!
                    _imageReconExList.value = _imageReconExList.value!!.drop(1)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun givePoints(userId: String) {
        usersRef.child(userId)
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

    fun getImageReconEx() {
        imageReconExRef.child(_imageReconExList.value?.first()?.exerciseName!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _currentEx.value = snapshot.getValue<ImageRecognitionExercise>()!!
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


    fun addImageReconExAnswer() {
        val answerId = randomString()
        val imageAnswer = ImageReconAnswer(answerId, _imageReconExList.value?.first()?.assignId!!, answerCorrect, Instant.now().toEpochMilli())
        answersRef.child(answerId).setValue(imageAnswer)
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

    fun getAssignedImageReconEx(userId: String) {
        assignedExercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AssignedExercise>()
                if (snapshot.exists()) {
                    for (assignedExercise in snapshot.children) {
                        val exercise = assignedExercise.getValue<AssignedExercise>()!!
                        if (exercise.userId == userId && exercise.exerciseType == "Image Recognition Exercise") {
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
                                                val imageReconAnswer = child.getValue<ImageReconAnswer>()
                                                val checkIsSameDay = checkIsSameDay(todayInMillis, imageReconAnswer?.ansDate!!)
                                                if (exercise.assignId.contentEquals(imageReconAnswer.assignId) && checkIsSameDay) {
                                                    answeredToday = true
                                                }
                                            }
                                            if (!answeredToday) {
                                                currentList =
                                                    currentList + exercise
                                                _imageReconExList.postValue(currentList)
                                            }
                                        } else {
                                            currentList =
                                                currentList + exercise
                                            _imageReconExList.postValue(currentList)
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