package com.example.dald.Phone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(var context: Context, var contactsList: List<ContactModel>, var listener: SelectListener) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.contact_recycler_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var contact = contactsList[position]
        holder.contactName.text = contact.name
        holder.contactNumber.text = contact.number

        holder.contactCard.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                listener.onItemClicked(contactsList[position])
            }
        })

//        if (contact.image != null) {
//            Picasso.get().load(contact.image).into(holder.contactImage)
//        } else {
//            holder.contactImage.setImageResource(R.mipmap.ic_launcher_round)
//        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    /**
     * Custom ViewHolder class
     */
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contactName: TextView = itemView.findViewById(R.id.tv_ContactName)
        var contactNumber: TextView = itemView.findViewById(R.id.tv_ContactNumber)
        var contactImage: CircleImageView = itemView.findViewById(R.id.civ_ContactImage)
        var contactCard: CardView = itemView.findViewById(R.id.cv_ContactCard)
    }
}