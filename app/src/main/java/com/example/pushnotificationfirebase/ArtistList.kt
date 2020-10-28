package com.example.pushnotificationfirebase

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ArtistList(private val context: Activity, private val artistList: List<Artist>) :
    ArrayAdapter<Artist?>(context, R.layout.list_layout, artistList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem: View = inflater.inflate(R.layout.list_layout, null, true)
        val textViewName = listViewItem.findViewById<View>(R.id.textViewName) as TextView
        val textViewGenre = listViewItem.findViewById<View>(R.id.textViewGenre) as TextView
        val artist = artistList[position]
        textViewName.setText(artist.artistName)
        textViewGenre.setText(artist.spinnerGenres)
        return listViewItem
    }
}
