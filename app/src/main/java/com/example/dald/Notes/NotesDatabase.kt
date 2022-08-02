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
    private val Column_NotesText = "Text"
    private val Column_Checked = "Checked"


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlStatement = "CREATE TABLE " + Table_Notes + " ( " +
                    Column_NotesID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Column_NotesText + " TEXT NOT NULL, " +
                    Column_Checked + " INTEGER NOT NULL ) "

            db?.execSQL(sqlStatement)

        } catch (e: SQLException) {
            e.message
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun addNote(text: String, checked: Int) {
        // 'writeableDatabase' method used on this database to allow it to be edited
        val db: SQLiteDatabase = this.writableDatabase
        // ContentValues stores the values which are to be used for the database
        val cv: ContentValues = ContentValues()
        cv.put(Column_NotesText, text)
        cv.put(Column_Checked, checked)
        // inserts the values from 'cv' (ContentValues()) into the database
        val success = db.insert(Table_Notes, null, cv)
        db.close()
    }

    /**
     * Returns the list of NotesModel objects
     */
    fun getAllNotes(): MutableList<NotesModel> {
        /* try connecting to database */
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            throw SQLException("Cannot connect to database!")
        }

        val sqlStatement = "SELECT * FROM $Table_Notes"
        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        val notesModelList: MutableList<NotesModel>? = null

        // if QuestionAnswer is found
        if (cursor.moveToFirst()) {
            // do-while loop to add all QuestionAnswers
            do {
                val notesText = cursor.getString(1)
                val checked = cursor.getString(2)

                val questionAnswer = NotesModel(notesText, checked.toString().toBoolean())
                notesModelList?.add(questionAnswer)
            } while (cursor.moveToNext())
        }
        return notesModelList!!
    }

}