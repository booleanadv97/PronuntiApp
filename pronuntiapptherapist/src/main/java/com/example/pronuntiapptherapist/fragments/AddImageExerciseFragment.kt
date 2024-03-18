package com.example.pronuntiapptherapist.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.pronuntiapptherapist.databinding.FragmentAddImageExerciseBinding
import com.example.pronuntiapptherapist.models.ImageExercise
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence


class AddImageExerciseFragment : Fragment() {
    private lateinit var binding: FragmentAddImageExerciseBinding
    private lateinit var imageUri: Uri
    private var imgName: String? = ""
    private val imgExercisesRef =
        FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "Image Exercises"
        )
    private val STRING_LENGTH = 32
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val storageRef = Firebase.storage.reference
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                if (result.data?.data != null) {
                    imageUri = result.data?.data!!
                    imgName = getFileName(requireContext(), imageUri)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUpload.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }
        binding.buttonSubmit.setOnClickListener() {
            if (binding.editTextImageExerciseName.toString().isEmpty() ||
                binding.textViewExerciseDescription.toString().isEmpty() ||
                imgName!!.isEmpty()
            ) {
                Toast.makeText(
                    context, "Devi compilare tutti i campi",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                imgExercisesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(binding.editTextImageExerciseName.text.toString())) {
                            Toast.makeText(
                                context, "L'esercizio è già presente nel sistema",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val imageId = randomString()
                            val uTask = uploadImgToDB(imageUri, imageId)
                            uTask.addOnSuccessListener {
                                storageRef.child("imageExercises")
                                    .child(imageId).downloadUrl.addOnSuccessListener { uri ->
                                        val downloadUrl = uri.toString()
                                        imgExercisesRef.child(binding.editTextImageExerciseName.text.toString())
                                            .setValue(
                                                ImageExercise(
                                                    binding.editTextImageExerciseName.text.toString(),
                                                    binding.textViewExerciseDescription.text.toString(),
                                                    downloadUrl,
                                                    imageId
                                                )
                                            )
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context, "Exercise created",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }.addOnFailureListener {
                                                Log.d(
                                                    "$context -> Save exercise to RTDB",
                                                    "Failed to save record : ${it.stackTraceToString()}"
                                                )
                                                storageRef.child("imageExercises").child(imageId)
                                                    .delete()
                                                Toast.makeText(
                                                    context, "Exercise save failed",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                    }.addOnFailureListener {
                                        Log.d(
                                            "$context -> Upload image",
                                            "Image Upload failed  ${it.stackTraceToString()}"
                                        )
                                    }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    context, "Image upload failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(
                                    "$context -> Upload image",
                                    "Image $imageUri Upload fail ${it.stackTraceToString()}"
                                )
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImageExerciseBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun uploadImgToDB(imageUri: Uri, imageId: String): UploadTask {
        return storageRef.child("imageExercises/$imageId").putFile(imageUri)
    }

    private fun randomString(): String =
        ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
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