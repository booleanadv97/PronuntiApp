package com.example.pronuntiapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.ImageReconAnswer
import com.example.pronuntiapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class ImageReconAnswersGridViewAdapter(private val context: Context,
                                       private val answers: List<ImageReconAnswer>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return answers.size
    }

    override fun getItem(position: Int): ImageReconAnswer {
        return answers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.image_recon_answer_gridview_item, parent, false)
        val answer : ImageReconAnswer = getItem(position)
        val txtAnswerDate: TextView = rowView.findViewById(R.id.txtAnswerDate)
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(answer.ansDate)
        val txtAnswerDateTxt = "${context.getString(R.string.txt_assignment_answer_date)} : $date"
        txtAnswerDate.text = txtAnswerDateTxt
        val txtAnswerCheck: TextView = rowView.findViewById(R.id.txtAnswerResult)
        val txtAnswerCheckTxt : String = if(answer.answerCorrect == "Yes")
            "${context.getString(R.string.txt_assignment_answer_correct)} : Si"
        else
            "${context.getString(R.string.txt_assignment_answer_correct)} : No"
        txtAnswerCheck.text = txtAnswerCheckTxt
        return rowView
    }
}