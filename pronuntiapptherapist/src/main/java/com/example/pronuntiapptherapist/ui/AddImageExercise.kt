package com.example.pronuntiapptherapist.ui
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pronuntiapptherapist.databinding.ActivityAddImageExerciseBinding
import com.example.pronuntiapptherapist.models.GridViewModal
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class AddImageExercise : AppCompatActivity() {
    private lateinit var binding : ActivityAddImageExerciseBinding
    private lateinit var imageUri : Uri
    private var imgName : String? = ""
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    private val imgExercisesRef = database.getReference("Image Exercises")
    private val STRING_LENGTH = 32
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val storageRef = Firebase.storage.reference
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                imageUri = result.data?.data!!
                imgName = getFileName(applicationContext, imageUri)
            }
        }

    private fun uploadImgToDB(imageUri: Uri, imageId: String): UploadTask {
        return storageRef.child("imageExercises/$imageId").putFile(imageUri)
    }
    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddImageExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonUpload.setOnClickListener {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                imagePickerActivityResult.launch(galleryIntent)
        }
        binding.buttonSubmit.setOnClickListener(){
            if(binding.editTextImageExerciseName.toString().isEmpty() ||
                binding.textViewExerciseDescription.toString().isEmpty() ||
                imgName!!.isEmpty()) {
                Toast.makeText(
                    this@AddImageExercise, "Devi compilare tutti i campi",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                val imageId = randomString()
                val uTask = uploadImgToDB(imageUri, imageId)
                uTask.addOnSuccessListener {
                    storageRef.child("imageExercises").child(imageId).downloadUrl.addOnSuccessListener {
                        uri -> val downloadUrl = uri.toString()
                        imgExercisesRef.child(imageId)
                            .setValue(GridViewModal(binding.editTextImageExerciseName.text.toString(),
                                binding.textViewExerciseDescription.text.toString(),
                                downloadUrl
                            ))
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@AddImageExercise, "Exercise created",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener{
                                Log.d("${this@AddImageExercise::class.java} -> Save exercise to RTDB","Failed to save record : ${it.stackTraceToString()}")
                                storageRef.child("imageExercises").child(imageId).delete()
                                Toast.makeText(
                                    this@AddImageExercise, "Exercise save failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }.addOnFailureListener{
                        Log.d("${this@AddImageExercise::class.java} -> Upload image", "Image Upload failed  ${it.stackTraceToString()}")
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this@AddImageExercise, "Image upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("${this@AddImageExercise::class.java} -> Upload image", "Image $imageUri Upload fail ${it.stackTraceToString()}")
                }
            }
        }
    }
    private fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
}