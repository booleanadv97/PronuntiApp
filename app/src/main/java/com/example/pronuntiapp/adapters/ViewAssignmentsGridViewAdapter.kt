package com.example.pronuntiapp.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.AssignedExercise
import com.example.pronuntiapp.R


internal class ViewAssignmentsGridViewAdapter(
    private val assignmentsList: List<AssignedExercise>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var txtExerciseName: TextView
    private lateinit var txtExerciseType: TextView
    private lateinit var txtStartDate: TextView
    private lateinit var txtEndDate: TextView
    private lateinit var txtCheck: TextView
    override fun getCount(): Int {
        return assignmentsList.size
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
            convertView = layoutInflater!!.inflate(R.layout.view_parent_assignments_gridview_item, null)
        }
        txtExerciseName = convertView!!.findViewById(R.id.txtExerciseName)
        txtExerciseType = convertView.findViewById(R.id.txtExerciseType)
        txtStartDate = convertView.findViewById(R.id.txtStartDate)
        txtEndDate = convertView.findViewById(R.id.txtEndDate)
        txtCheck = convertView.findViewById(R.id.txtCheck)
        val txtExerciseNameTxt = "${context.getString(R.string.txt_exercise_name)} : ${assignmentsList[position].exerciseName}"
        txtExerciseName.text = txtExerciseNameTxt
        val exerciseType = context.resources.getString(R.string.txtExerciseTypeTxt)
        val txtExerciseTypeTxt = "$exerciseType: ${assignmentsList[position].exerciseType}"
        txtExerciseType.text = txtExerciseTypeTxt
        val startDate = context.resources.getString(R.string.txtStartDateTxt)
        val txtStartDateTxt = "$startDate:  ${assignmentsList[position].startDate}"
        txtStartDate.text = txtStartDateTxt
        val endDate = context.resources.getString(R.string.txtEndDateTxt)
        val txtEndDateTxt = "$endDate:  ${assignmentsList[position].endDate}"
        txtEndDate.text = txtEndDateTxt
        val txtCheckTxt = if(assignmentsList[position].therapistCheck!!.isNotEmpty())
            assignmentsList[position].therapistCheck!!
        else
            "No"
        txtCheck.text = txtCheckTxt
        return convertView
    }
}