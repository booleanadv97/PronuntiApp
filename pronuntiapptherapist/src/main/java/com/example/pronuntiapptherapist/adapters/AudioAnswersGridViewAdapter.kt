package com.example.pronuntiapptherapist.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.common_utils.models.AudioAnswer
import com.example.common_utils.models.MediaPlayerManager
import com.example.pronuntiapptherapist.R
import java.text.SimpleDateFormat
import java.util.Locale

class AudioAnswersGridViewAdapter(private val context: Context,
                                  private val answers: List<AudioAnswer>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return answers.size
    }

    override fun getItem(position: Int): AudioAnswer {
        return answers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.audio_answer_gridview_item, parent, false)
        val answer : AudioAnswer = getItem(position)
        val txtAnswerDate: TextView = rowView.findViewById(R.id.txtAnswerDate)
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(answer.ansDate)
        val txtAnswerDateTxt = "${context.getString(R.string.txt_assignment_answer_date)} : $date"
        txtAnswerDate.text = txtAnswerDateTxt
        val btnPlay: Button = rowView.findViewById(R.id.btnPlay)
        val btnStop: Button = rowView.findViewById(R.id.btnStop)
        val mediaPlayer = MediaPlayerManager(context)
        btnStop.isEnabled = false
        btnPlay.setOnClickListener{
            btnPlay.isEnabled = false
            btnStop.isEnabled = true
            mediaPlayer.playAudio(answer.audioUrl!!)
            mediaPlayer.mediaPlayer!!.setOnCompletionListener {
                btnStop.isEnabled = false
                btnPlay.isEnabled = true
            }
        }
        btnStop.setOnClickListener{
            btnStop.isEnabled = false
            btnPlay.isEnabled = true
            mediaPlayer.stopPlayingAudio()
        }
        return rowView
    }
}