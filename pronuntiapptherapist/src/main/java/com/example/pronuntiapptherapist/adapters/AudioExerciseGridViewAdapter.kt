package com.example.pronuntiapptherapist.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.models.AudioExercise.AudioExercise


internal class AudioExerciseGridViewAdapter(
    private val exerciseList: List<AudioExercise>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var exerciseNameTV: TextView
    private lateinit var exerciseDescriptionTV: TextView
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
            convertView = layoutInflater!!.inflate(R.layout.audio_exercises_gridview_item, null)
        }
        exerciseNameTV = convertView!!.findViewById(R.id.txtAudioExerciseName)
        exerciseNameTV.text = exerciseList[position].exerciseName
        exerciseDescriptionTV = convertView.findViewById(R.id.txtAudioExerciseDescription)
        exerciseDescriptionTV.text = exerciseList[position].exerciseDescription
        return convertView
    }
}