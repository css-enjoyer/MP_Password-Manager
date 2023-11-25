package com.example.mobproj

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "password_db"

        private const val TABLE_PASSWORDS = "passwords"
        private const val COL_TITLE = "pw_title"
        private const val COL_ID = "pw_id"
        private const val COL_DESC = "pw_desc"
        private const val COL_PW = "password"
    }

    // method for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_PASSWORDS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_TITLE TEXT," +
                "$COL_DESC TEXT," +
                "$COL_PW TEXT)"
        db.execSQL(query)
    }

    fun addPassword(pw: Password): Boolean {
        // create variable for db and call writable to write
        val db = this.writableDatabase
        val values = ContentValues()

        // pass all values with kv pair
        values.put(COL_TITLE, pw.pwTitle)
        values.put(COL_DESC, pw.pwDesc)
        values.put(COL_PW, pw.password)

        // pass values to table
        val success = db.insert(TABLE_PASSWORDS, null, values)
        db.close()
        return success > -1
    }
    @SuppressLint("Range")
    fun getPassword(): List<Password> {
        val pwList: ArrayList<Password> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_PASSWORDS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var pwID: Int
        var pwTitle: String
        var pwDesc: String
        var password: String

        if (cursor.moveToFirst()) {
            do {
                pwID = cursor.getInt(cursor.getColumnIndex(COL_ID))
                pwTitle = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                pwDesc = cursor.getString(cursor.getColumnIndex(COL_DESC))
                password = cursor.getString(cursor.getColumnIndex(COL_PW))

                // check retrieved passwords
                Log.d("DBHandler", "Retrieved Password - ID: $pwID, Title: $pwTitle, Desc: $pwDesc, Password: $password")

                val pw = Password(pwID = pwID, pwTitle = pwTitle, pwDesc = pwDesc, password = password)
                pwList.add(pw)
            } while (cursor.moveToNext())
        }
        return pwList
    }

    fun updatePassword(pw: Password): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, pw.pwTitle)
        contentValues.put(COL_DESC, pw.pwDesc)
        contentValues.put(COL_PW, pw.password)

        val success = db.update(TABLE_PASSWORDS, contentValues, "$COL_ID=?", arrayOf(pw.pwID.toString()))
        db.close()
        return success
    }

    fun deletePassword(pw: Password): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, pw.pwID)
        val success = db.delete(TABLE_PASSWORDS, "id=" + pw.pwID, null)
        db.close()
        return success
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PASSWORDS")
        onCreate(db)
    }
}
