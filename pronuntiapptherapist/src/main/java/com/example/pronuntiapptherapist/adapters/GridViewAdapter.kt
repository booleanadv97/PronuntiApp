package com.example.pronuntiapptherapist.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pronuntiapptherapist.R
import com.example.pronuntiapptherapist.models.GridViewModal
import com.squareup.picasso.Picasso


internal class GridViewAdapter(
    private val exerciseList: List<GridViewModal>,
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.image_exercises_gridview_item, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        exerciseIV = convertView!!.findViewById(R.id.idIVCourse)
        exerciseTV = convertView.findViewById(R.id.idTVCourse)
        // on below line we are setting image for our course image view.
        Picasso.get().load(exerciseList[position].url).into(exerciseIV)
        // on below line we are setting text in our course text view.
        exerciseTV.text = exerciseList[position].exerciseName
        // at last we are returning our convert view.
        return convertView
    }
}