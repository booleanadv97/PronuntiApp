package com.example.pronuntiapptherapist.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.User
import com.example.pronuntiapptherapist.R


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
        val emailTvText = "E-mail: ${parentsList[position].email}"
        parentEmailTV = convertView!!.findViewById(R.id.txtEmail)
        parentEmailTV.text = emailTvText
        val firstNameTvText = "Cognome: ${parentsList[position].firstName}"
        parentFirstNameTV = convertView.findViewById(R.id.txtFirstName)
        parentFirstNameTV.text = firstNameTvText
        val lastNameTvText =  "Nome: ${parentsList[position].lastName}"
        parentLastNameTV = convertView.findViewById(R.id.txtLastName)
        parentLastNameTV.text = lastNameTvText
        return convertView
    }
}