package com.example.dald.Photos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dald.R

class PhotoAdapter (private var context: Context, private var imagesList: ArrayList<PhotoPath>) :
    RecyclerView.Adapter<PhotoAdapter.ImageViewHolder>() {

    // custom ViewHolder class
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView? = null
        init {
            image = itemView.findViewById(R.id.iv_ImageRow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.photo_recycler_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = imagesList[position]
        Glide.with(context).load(currentImage.imagePath).apply(RequestOptions().centerCrop()).into(holder.image!!)

        holder.image?.setOnClickListener{
            val intent = Intent(context, PhotoFullActivity::class.java)
            intent.putExtra("path", currentImage.imagePath)
            intent.putExtra("name", currentImage.imageName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}