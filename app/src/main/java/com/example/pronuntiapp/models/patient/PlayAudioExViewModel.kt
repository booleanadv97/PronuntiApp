package com.example.pronuntiapp.models.patient

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AssignedExercise
import com.example.common_utils.models.AudioAnswer
import com.example.common_utils.models.AudioExercise
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

class PlayAudioExViewModel : ViewModel() {
    val database =
        FirebaseDatabase.getInstance()
    private val assignedExercisesRef = database.getReference("Assigned Exercises")
    private val audioExRef = database.getReference("Audio Exercises")
    private val answersRef = database.getReference("Audio Exercises Answers")
    private val usersRef = database.getReference("users")
    lateinit var outputMP4File: String
    private var audioUrl: String? = ""
    lateinit var mp4Uri: Uri
    private val STRING_LENGTH = 32
    val ADD_ANSWER_RESULT_OK = "OK"
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    var audioAnsId: String? = ""
    private var _audioExList: MutableLiveData<List<AssignedExercise>> = MutableLiveData(emptyList())
    val audioExList: LiveData<List<AssignedExercise>> = _audioExList
    private var _currentAudioEx = MutableLiveData<AudioExercise>()
    val currentAudioEx: LiveData<AudioExercise> = _currentAudioEx
    private val storageRef = Firebase.storage.reference
    private val _addAnswerResult = MutableLiveData<String>()
    val addAnswerResult: LiveData<String> = _addAnswerResult
    private val _rewardType = MutableLiveData<String>()
    val rewardType: LiveData<String> = _rewardType
    private val _reward = MutableLiveData<String>()
    val reward: LiveData<String> = _reward
    private var _userHero = MutableLiveData<String>()
    val userHero : LiveData<String> = _userHero
    var hasAnswered : Boolean = false

    fun getHero(userId : String){
        usersRef.child(userId).child("character").get().addOnSuccessListener {
            _userHero.value = it.value.toString()
        }
    }
    fun resetAddAnswerResult() {
        _addAnswerResult.value = ""
    }

    fun getRewards() {
        assignedExercisesRef.child(_audioExList.value?.first()?.assignId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _rewardType.value = snapshot.getValue<AssignedExercise>()?.rewardType!!
                    _reward.value = snapshot.getValue<AssignedExercise>()?.reward!!
                    _audioExList.value = _audioExList.value!!.drop(1)
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

    fun getAudioEx() {
        audioExRef.child(_audioExList.value?.first()?.exerciseName!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _currentAudioEx.value = snapshot.getValue<AudioExercise>()!!
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
        storageRef.child("answers/audioExercises/$audioAnsId").putFile(mp4Uri)
            .addOnSuccessListener {
                val audioRef = storageRef.child("answers/audioExercises/$audioAnsId")
                val downloadUrlTask = audioRef.getDownloadUrl()
                downloadUrlTask.addOnSuccessListener { uri ->
                    audioUrl = uri.toString()
                    addAudioExAnswer(_audioExList.value?.first()?.assignId!!)
                }
            }
    }

    private fun addAudioExAnswer(assignId: String) {
        val audioAnswer = AudioAnswer(audioAnsId, audioUrl, assignId, Instant.now().toEpochMilli())
        answersRef.child(audioAnsId!!).setValue(audioAnswer)
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

    fun getAssignedAudioEx(userId: String) {
        assignedExercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentList = emptyList<AssignedExercise>()
                if (snapshot.exists()) {
                    for (assignedExercise in snapshot.children) {
                        val exercise = assignedExercise.getValue<AssignedExercise>()!!
                        if (exercise.userId == userId && exercise.exerciseType == "Audio Exercise") {
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
                                                val audioAnswer = child.getValue<AudioAnswer>()
                                                val checkIsSameDay = checkIsSameDay(todayInMillis, audioAnswer?.ansDate!!)
                                                if (exercise.assignId.contentEquals(audioAnswer.assignId) && checkIsSameDay) {
                                                    answeredToday = true
                                                }
                                            }
                                            if (!answeredToday) {
                                                currentList =
                                                    currentList + exercise
                                                _audioExList.postValue(currentList)
                                            }
                                        } else {
                                            currentList =
                                                currentList + exercise
                                            _audioExList.postValue(currentList)
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