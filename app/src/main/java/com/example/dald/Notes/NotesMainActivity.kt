package com.example.dald.Notes

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.MainActivity
import com.example.dald.R

class NotesMainActivity : AppCompatActivity() {
    // create instance if TodoAdapter
    private lateinit var todoAdapter: NotesAdapter
    lateinit var rvNoteItems: RecyclerView
    lateinit var btnAddNote: Button
    lateinit var etNoteText: EditText
    lateinit var btnDeleteCompletedNotes: Button

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_main)

        hideSystemUI()
        supportActionBar?.hide()

        // initialise todoAdapter
        todoAdapter = NotesAdapter(mutableListOf())

        rvNoteItems = findViewById(R.id.rv_NoteItems)
        btnAddNote = findViewById(R.id.btn_AddNote)
        etNoteText = findViewById(R.id.et_NoteText)
        btnDeleteCompletedNotes = findViewById(R.id.btn_DeleteCompletedNotes)

        // attach todoAdapter to the rvTodoItems RecyclerView
        rvNoteItems.adapter = todoAdapter
        rvNoteItems.layoutManager = LinearLayoutManager(this)


        // set onClickListener for btnAddTodo
        btnAddNote.setOnClickListener {
            val todoTitle = etNoteText.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = NotesModel(todoTitle)
                todoAdapter.addTodo(todo)
                etNoteText.text.clear()
            }
        }

        // set onClickListener for btnDeleteDoneTodos
        btnDeleteCompletedNotes.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun returnHomeFromNotes(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}