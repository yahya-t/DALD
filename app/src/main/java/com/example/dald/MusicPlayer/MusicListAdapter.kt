package com.example.dald.MusicPlayer

import com.example.dald.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easytutomusicapp.MusicModel


class MusicListAdapter(var musicList: ArrayList<MusicModel>, var context: Context) : RecyclerView.Adapter<MusicListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.music_recyler_item, parent, false)
        return MusicListAdapter.CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var musicData = musicList.get(position)
        holder.titleTextView.setText(musicData.title)


    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var iconImageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.music_title)
            iconImageView = itemView.findViewById(R.id.music_icon)
        }
    }

}