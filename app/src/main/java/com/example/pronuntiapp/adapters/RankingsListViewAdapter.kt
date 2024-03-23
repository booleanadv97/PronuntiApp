package com.example.pronuntiapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.common_utils.models.User
import com.example.pronuntiapp.R

@Suppress("DEPRECATION")
class RankingsListViewAdapter(private val context: Context,
                              private val userList: List<User>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): User {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.rankings_listview_item, parent, false)
        val txtRank: TextView = rowView.findViewById(R.id.txtRank)
        val rank = position + 1
        val txtPoints : TextView = rowView.findViewById(R.id.txtPoints)
        when(rank){
            1 -> txtRank.setTextColor(context.resources.getColor(R.color.gold))
            2 -> txtRank.setTextColor(context.resources.getColor(R.color.silver))
            3 -> txtRank.setTextColor(context.resources.getColor(R.color.bronze))
            else -> txtRank.setTextColor(context.resources.getColor(R.color.white))
        }
        txtPoints.text = userList[position].points.toString()
        txtRank.text = rank.toString()
        val txtUser : TextView = rowView.findViewById(R.id.txtUser)
        txtUser.text = userList[position].firstName
        return rowView
    }
}