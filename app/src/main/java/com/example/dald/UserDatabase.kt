package com.example.dald

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DatabaseName = "UserDatabase.db"
private val version: Int = 1

class UserDatabase(context: Context) : SQLiteOpenHelper(context, DatabaseName, null, version) {

    /**** Table User ****/
    private val Table_User = "Table_User"
    private val Column_UserID = "UserID"
    private val Column_UserName = "UserName"


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlStatement = "CREATE TABLE " + Table_User + " ( " +
                    Column_UserID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Column_UserName + " TEXT ) "

            db?.execSQL(sqlStatement)

        } catch (e: SQLException) {
            e.message
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addUserDB(text: String) {
        // 'writeableDatabase' method used on this database to allow it to be edited
        val db: SQLiteDatabase = this.writableDatabase
        // ContentValues stores the values which are to be used for the database
        val cv: ContentValues = ContentValues()
        cv.put(Column_UserName, text)
        // inserts the values from 'cv' (ContentValues()) into the database
        val success = db.insert(Table_User, null, cv)
        db.close()
    }


    fun deleteUserDB() {
        // writableDatabase for delete actions
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(Table_User, null, null)
        db.close()
    }

    fun getUser(): UserModel {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $Table_User"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)
        if (cursor.moveToFirst()) {
            // The ID is found
            db.close()
            return UserModel(cursor.getString(1))
        } else {
            db.close()
            return UserModel("") // not found
        }
    }

}