package com.example.pronuntiapptherapist.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.common_utils.models.ImageRecognitionExercise
import com.example.pronuntiapptherapist.R
import com.squareup.picasso.Picasso

class ImageRecognitionExerciseGridViewAdapter(private val exerciseList: List<ImageRecognitionExercise>,
                                              private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var exerciseTV: TextView
    private lateinit var exerciseIV: ImageView
    override fun getCount(): Int {
        return exerciseList.size
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
            convertView = layoutInflater!!.inflate(R.layout.image_exercises_gridview_item, null)
        }
        exerciseIV = convertView!!.findViewById(R.id.idIVCourse)
        exerciseTV = convertView.findViewById(R.id.idTVCourse)
        Picasso.get().load(exerciseList[position].urlCorrectAnswerImage).into(exerciseIV)
        exerciseTV.text = exerciseList[position].exerciseName
        return convertView
    }
}