package com.example.pronuntiapptherapist.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.models.User


internal class ManageParentsGridViewAdapter(
    private val parentsList: List<User>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var parentEmailTV: TextView
    private lateinit var parentFirstNameTV: TextView
    private lateinit var parentLastNameTV: TextView
    override fun getCount(): Int {
        return parentsList.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertVieww: View?, parent: ViewGroup?): View {
        var convertView = convertVieww
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.manage_parents_gridview_item, null)
        }
        parentEmailTV = convertView!!.findViewById(R.id.txtEmail)
        parentEmailTV.text = "E-mail: ${parentsList[position].email}"
        parentFirstNameTV = convertView!!.findViewById(R.id.txtFirstName)
        parentFirstNameTV.text = "Cognome: ${parentsList[position].firstName}"
        parentLastNameTV = convertView!!.findViewById(R.id.txtLastName)
        parentLastNameTV.text = "Nome: ${parentsList[position].lastName}"
        return convertView
    }
}