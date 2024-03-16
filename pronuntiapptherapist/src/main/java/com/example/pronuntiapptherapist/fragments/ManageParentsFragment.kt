package com.example.pronuntiapptherapist.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class ManageParentsFragment : Fragment() {
    val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app")
    val usersRef = database.getReference("users")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_parents, container, false)
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)
        usersRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                    for(child in snapshot.children){
                        val tr1 = TableRow(context)
                        tr1.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        val textViewFName = TextView(context)
                        textViewFName.text = child.getValue<User>()?.email
                        tr1.addView(textViewFName)
                        val textViewFName2 = TextView(context)
                        textViewFName2.text = child.getValue<User>()?.firstName
                        tr1.addView(textViewFName2)
                        val textViewFName3 = TextView(context)
                        textViewFName3.text = child.getValue<User>()?.lastName
                        tr1.addView(textViewFName3)
                        tableLayout.addView(
                            tr1, TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT
                            )
                        )
                    }
            }
            override fun onCancelled(error: DatabaseError) {
               Log.d("${ManageParentsFragment::class.java} -> usersRef","read canceled", error.toException())
            }
        })
        return view
    }
}