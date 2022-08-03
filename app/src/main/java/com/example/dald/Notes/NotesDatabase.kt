package com.example.dald.Notes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

private val DatabaseName = "NotesDatabase.db"
private val version: Int = 1

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DatabaseName, null, version) {

    /**** Table Notes ****/
    private val Table_Notes = "Table_Notes"
    private val Column_NotesID = "NotesID"
    private val Column_NotesText = "NotesText"


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlStatement = "CREATE TABLE " + Table_Notes + " ( " +
                    Column_NotesID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Column_NotesText + " TEXT) "

            db?.execSQL(sqlStatement)

        } catch (e: SQLException) {
            e.message
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun addNoteDB(text: String) {
        // 'writeableDatabase' method used on this database to allow it to be edited
        val db: SQLiteDatabase = this.writableDatabase
        // ContentValues stores the values which are to be used for the database
        val cv: ContentValues = ContentValues()
        cv.put(Column_NotesText, text)
        // inserts the values from 'cv' (ContentValues()) into the database
        val success = db.insert(Table_Notes, null, cv)
        db.close()
    }

    fun deleteNoteDB(note: String) {
        // writableDatabase for delete actions
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(Table_Notes, "$Column_NotesText = \"$note\"", null) == 1
        db.close()
    }

    /**
     * Returns the list of NotesModel objects
     */
    fun getAllNotesDB(): MutableList<NotesModel> {
        /* try connecting to database */
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            throw SQLException("Cannot connect to database!")
        }

        val sqlStatement = "SELECT * FROM $Table_Notes"
        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        val notesModelList = mutableListOf<NotesModel>()

        // if note is found
        if (cursor.moveToFirst()) {
            // do-while loop to add all notes
            do {
                val notesText = cursor.getString(1)

                val questionAnswer = NotesModel(notesText)
                notesModelList?.add(questionAnswer)
            } while (cursor.moveToNext())
        }
        return notesModelList
    }

}