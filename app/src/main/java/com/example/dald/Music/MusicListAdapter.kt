package com.example.dald.Music

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.R

class MusicListAdapter(var musicList: ArrayList<MusicModel>, var context: Context) : RecyclerView.Adapter<MusicListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.music_recyler_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val songData: MusicModel = musicList[position]
        holder.musicTitle.text = songData.title

        if (CustomMediaPlayer.currentIndex == position) {
            holder.musicTitle.setTextColor(Color.parseColor("#FF0000"))
        } else {
            holder.musicTitle.setTextColor(Color.parseColor("#000000"))
        }

        holder.itemView.setOnClickListener { //navigate to another acitivty
            CustomMediaPlayer.getInstance()?.reset()
            CustomMediaPlayer.currentIndex = position
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("LIST", musicList)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var musicTitle: TextView
        var musicIcon: ImageView

        init {
            musicTitle = itemView.findViewById(R.id.music_title)
            musicIcon = itemView.findViewById(R.id.music_icon)
        }
    }

}