package com.example.pronuntiapp.models

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.common_utils.models.AudioExercise
import com.example.common_utils.models.ImageExercise
import com.example.common_utils.models.ImageRecognitionExercise
import com.example.common_utils.models.User
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class MainActivityViewModel : ViewModel() {
    private val STRING_LENGTH = 32
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val storageRef = Firebase.storage.reference
    private val audioExercisesRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Audio Exercises"
        )
    private val imageExercisesRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Image Exercises"
        )
    private val imageRecognitionRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Image Recognition Exercises"
        )
    val defaultEmail = "default@user.user"
    val defaultFirstName = "Default"
    val defaultLastName = "User"
    val defaultPassword = "Default"
    var audio_exercise_1_uri : Uri? = null
    var audio_exercise_2_uri : Uri? = null
    var audio_exercise_3_uri : Uri? = null
    var image_exercise_1_uri : Uri? = null
    var image_exercise_2_uri : Uri? = null
    var image_recon_correct_img_1 : Uri? = null
    var image_recon_correct_img_2 : Uri? = null
    var image_recon_alt_img_1 : Uri? = null
    var image_recon_alt_img_2 : Uri? = null
    var image_recon_audio_1 : Uri? = null
    var image_recon_audio_2 : Uri? = null

    private fun addDefaultImagesExercise(exerciseName : String, exerciseDescription : String, uri : Uri){
        val imageId = randomString()
        val uTask = storageRef.child("imageExercises/$imageId").putFile(uri)
        uTask.addOnSuccessListener {
            storageRef.child("imageExercises")
                .child(imageId).downloadUrl.addOnSuccessListener { uri ->
                    val imgUrl = uri.toString()
                    imageExercisesRef.child(exerciseName)
                        .setValue(
                            ImageExercise(
                                exerciseName,
                                exerciseDescription,
                                imgUrl,
                                imageId
                            )
                        )
                }
        }
    }

    private fun addDefaultImageRecognitionExercise(exerciseName : String, exerciseDescription : String,
                                                    imageCorrectUri : Uri , imageAltUri : Uri, audio : Uri){
        val correctImageId = randomString()
        storageRef.child("imageRecognitionExercises/$correctImageId").putFile(imageCorrectUri).addOnSuccessListener {
            storageRef.child("imageRecognitionExercises/$correctImageId").downloadUrl.addOnSuccessListener {
                val imgCorrectUrl = it.toString()
                val altImageId = randomString()
                storageRef.child("imageRecognitionExercises/$altImageId").putFile(imageAltUri).addOnSuccessListener {
                    storageRef.child("imageRecognitionExercises/$altImageId").downloadUrl.addOnSuccessListener {
                        val imgAltUrl = it.toString()
                        val audioAnsId = randomString()
                        storageRef.child("audioRecognitionExercises/$audioAnsId").putFile(audio).addOnSuccessListener {
                            val audioRef = storageRef.child("audioRecognitionExercises/$audioAnsId")
                            val downloadUrlTask = audioRef.getDownloadUrl()
                            downloadUrlTask.addOnSuccessListener { uri ->
                                val audioUrl = uri.toString()
                                val newExercise = ImageRecognitionExercise(
                                    exerciseName,
                                    exerciseDescription,
                                    imgCorrectUrl,
                                    correctImageId,
                                    imgAltUrl,
                                    altImageId,
                                    audioAnsId,
                                    audioUrl)
                                imageRecognitionRef.child(newExercise.exerciseName.toString()).setValue(newExercise)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addDefaultAudioExercise(exerciseName : String, exerciseDescription : String, uri : Uri){
        val audioAnsId = randomString()
        storageRef.child("audioExercises/$audioAnsId").putFile(uri).addOnSuccessListener {
            val audioRef = storageRef.child("audioExercises/$audioAnsId")
            val downloadUrlTask = audioRef.getDownloadUrl()
            downloadUrlTask.addOnSuccessListener { uri ->
                val audioUrl = uri.toString()
                val newExercise = AudioExercise(
                    exerciseName,
                    exerciseDescription,
                    audioUrl,
                    audioAnsId)
                audioExercisesRef.child(newExercise.exerciseName.toString()).setValue(newExercise)
            }
        }
    }

    private fun addDefaultData(){
        addDefaultImagesExercise("Default image exercise 1",
            "Default image exercise description 1",
            image_exercise_1_uri!!)
        addDefaultImagesExercise("Default image exercise 2",
            "Default image exercise description 2",
            image_exercise_2_uri!!)
        addDefaultImageRecognitionExercise("Default image recon exercise 1",
            "Default image recon exercise description 1",
            image_recon_correct_img_1!!, image_recon_alt_img_1!!, image_recon_audio_1!!)
        addDefaultImageRecognitionExercise("Default image recon exercise 2",
            "Default image recon exercise description 2",
            image_recon_correct_img_2!!, image_recon_alt_img_2!!, image_recon_audio_2!!)
        addDefaultAudioExercise("Default audio exercise 1",
            "Default audio exercise description 1",
            audio_exercise_1_uri!!)
        addDefaultAudioExercise("Default audio exercise 2",
            "Default audio exercise description 2",
            audio_exercise_2_uri!!)
        addDefaultAudioExercise("Default audio exercise 3",
            "Default audio exercise description 3",
            audio_exercise_3_uri!!)
    }

    fun addUserToRTDB(uid : String){
        val ref = database.getReference("/users/$uid")
        val user = User(uid, defaultEmail, defaultFirstName, defaultLastName)
        ref.setValue(user)
        addDefaultData()
    }

    fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
}