package com.example.dald.Notes

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.R
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(private val notesList: MutableList<NotesModel>) :
    RecyclerView.Adapter<NotesAdapter.CustomViewHolder>() {

    /**
     * CustomViewHolder class extends RecyclerView.ViewHolder
     * @param itemView is the individual item in the RecyclerView
     */
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // return CustomViewHolder and inflate layout
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                // inflate note_item View
                R.layout.note_item,
                parent,
                false
            )
        )
    }

    // adds an item to the notes List
    fun addNote(note: NotesModel) {
        notesList.add(note)
        notifyItemInserted(notesList.size - 1)
    }

    // removes all items that are checked in the notes List
    fun deleteCompletedNotes() {
        notesList.removeAll { note ->
            note.isChecked
        }
        notifyDataSetChanged()
    }

    fun isChecked(): MutableList<NotesModel> {
        var checkedNotes = mutableListOf<NotesModel>()
        for (note in notesList) {
            if (note.isChecked) {
                checkedNotes?.add(note)
            }
        }
        return checkedNotes
    }

    /**
     * @param tvNoteText the textView which displays the text
     * @param isChecked value of the checkBox
     */
    private fun toggleStrikeThrough(tvNoteText: TextView, isChecked: Boolean) {
        // if checkBox is checked, put a strikethrough on the tvNoteText, else inverse (no strikethrough)
        if (isChecked) {
            tvNoteText.paintFlags = tvNoteText.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvNoteText.paintFlags = tvNoteText.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // get position in the notes List
        val cursorNote = notesList[position]

        // edit the individual itemView in the RecyclerView holder
        holder.itemView.apply {
            tv_NoteText.text = cursorNote.title
            cb_Complete.isChecked = cursorNote.isChecked
            // add tvNotesText and cbDone to the toggleStrikeThrough() method
            toggleStrikeThrough(tv_NoteText, cursorNote.isChecked)
            // add setonCheckedChangeLister{} to cbDone
            cb_Complete.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tv_NoteText, isChecked)
                cursorNote.isChecked = !cursorNote.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

}