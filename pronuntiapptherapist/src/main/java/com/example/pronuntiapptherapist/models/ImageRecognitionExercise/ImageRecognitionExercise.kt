package com.example.pronuntiapptherapist.models.ImageRecognitionExercise

data class ImageRecognitionExercise(
    val exerciseName : String? = "",
    val exerciseDescription : String? = "",
    val urlCorrectAnswerImage : String? = "",
    val imgCorrectAnswerId : String? = "",
    val urlAlternativeImage : String? = "",
    val imgAlternativeId : String? = "",
    val audioId : String? = "",
    val audioUrl : String? = ""
)
