package com.example.dald.Notes

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.R
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(private val notesList: MutableList<NotesModel>) : RecyclerView.Adapter<NotesAdapter.CustomViewHolder>() {

    /**
     * CustomViewHolder class extends RecyclerView.ViewHolder
     * @param itemView is the individual item in the RecyclerView
     */
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // return CustomViewHolder and inflate layout
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                // inflate item_todo View
                R.layout.note_item,
                parent,
                false
            )
        )
    }

    // adds an item to the todos List
    fun addTodo(todo: NotesModel) {
        notesList.add(todo)
        notifyItemInserted(notesList.size - 1)
    }

    // removes all items that are checked in the todos List
    fun deleteDoneTodos() {
        notesList.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    /**
     * @param tvTodoTitle the textView which displays the text
     * @param isChecked value of the checkBox
     */
    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        // if checkBox is checked, put a strikethrough on the tvTodoTitle, else inverse (no strikethrough)
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // get position in the todos List
        val curTodo = notesList[position]

        // edit the individual itemView in the RecyclerView holder
        holder.itemView.apply {
            tv_NoteText.text = curTodo.title
            cb_Complete.isChecked = curTodo.isChecked
            // add tvTodoTile and cbDone to the toggleStrikeThrough() method
            toggleStrikeThrough(tv_NoteText, curTodo.isChecked)
            // add setonCheckedChangeLister{} to cbDone
            cb_Complete.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tv_NoteText, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }

//        holder.itemView.setOnClickListener {
//            holder.itemView.setBackgroundColor(Color.RED)
//        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

}