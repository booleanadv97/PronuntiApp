package TherapistFragments

import TherapistViewModels.ParentsViewModel
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
import androidx.lifecycle.ViewModelProvider
import com.example.pronuntiapp.R
import com.example.pronuntiapp.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class ManageParentsFragment : Fragment() {
    private lateinit var viewModel: ParentsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_parents, container, false)
        viewModel = ViewModelProvider(this)[ParentsViewModel::class.java]
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)
        viewModel.usersRef.addValueEventListener(object : ValueEventListener {
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
                        textViewFName.setTextColor(R.color.black)
                        tr1.addView(textViewFName)
                        val textViewFName2 = TextView(context)
                        textViewFName2.text = child.getValue<User>()?.firstName
                        textViewFName2.setTextColor(R.color.black)
                        tr1.addView(textViewFName2)
                        val textViewFName3 = TextView(context)
                        textViewFName3.text = child.getValue<User>()?.lastName
                        textViewFName3.setTextColor(R.color.black)
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