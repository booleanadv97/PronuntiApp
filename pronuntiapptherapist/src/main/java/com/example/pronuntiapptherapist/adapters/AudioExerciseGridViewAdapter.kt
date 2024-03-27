
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.AudioExercise
import com.example.pronuntiapptherapist.R

class AudioExerciseGridViewAdapter(private val context: Context,
                                   private val dataSource: List<AudioExercise>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }


    override fun getItem(position: Int): Any {
        return dataSource[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.audio_exercises_listview_item, parent, false)
        val txtExerciseName: TextView = rowView.findViewById(R.id.txtAudioExerciseName)
        val txtExerciseDescription: TextView = rowView.findViewById(R.id.txtAudioExerciseDescription)
        val audioExercise = getItem(position) as AudioExercise
        val txtExerciseNameTxt = "${context.getString(R.string.txt_exercise_name)} : ${audioExercise.exerciseName}"
        txtExerciseName.text = txtExerciseNameTxt
        val txtExerciseDescriptionTxt = "${context.getString(R.string.txt_exercise_description)} : ${audioExercise.exerciseDescription}"
        txtExerciseDescription.text = txtExerciseDescriptionTxt
        return rowView
    }
}