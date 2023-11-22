import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "passwordDB"
        private const val DB_VERSION = 1
        private const val TABLE_PASSWORDS = "passwords"
        private const val TITLE_COL = "pwTitle"
        private const val ID_COL = "pwID"
        private const val DESC_COL = "pwDesc"
        private const val PW_COL = "password"
    }

    // method for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // create sqlite query, set col names and type
        val query = "CREATE TABLE $TABLE_PASSWORDS (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE_COL TEXT," +
                "$DESC_COL TEXT," +
                "$PW_COL TEXT)"
        // method to execute query
        db.execSQL(query)
    }

    fun addPassword(pw: Password): Long {
        // create variable for db and call writable to write
        val db = this.writableDatabase
        val values = ContentValues()

        // pass all values with kv pair
        values.put(ID_COL, pw.pwID)
        values.put(TITLE_COL, pw.pwTitle)
        values.put(DESC_COL, pw.pwDesc)
        values.put(PW_COL, pw.password)

        // pass values to table
        val success = db.insert(TABLE_PASSWORDS, null, values)
        db.close()
        return success
    }
    @SuppressLint("Range")
    fun getPassword(): List<Password> {
        val pwList: ArrayList<Password> = ArrayList<Password>()
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
                pwID = cursor.getInt(cursor.getColumnIndex("id"))
                pwTitle = cursor.getString(cursor.getColumnIndex("name"))
                pwDesc = cursor.getString(cursor.getColumnIndex("email"))
                password = cursor.getString(cursor.getColumnIndex("email"))
                val pw = Password(pwID = pwID, pwTitle = pwTitle, pwDesc = pwDesc, password = password)
                pwList.add(pw)
            } while (cursor.moveToNext())
        }
        return pwList
    }

    fun updatePassword(pw: Password): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_COL, pw.pwID)
        contentValues.put(TITLE_COL, pw.pwTitle)
        contentValues.put(DESC_COL, pw.pwDesc)
        contentValues.put(PW_COL, pw.password)

        val success = db.update(TABLE_PASSWORDS, contentValues, "id=" + pw.pwID, null)
        db.close()
        return success
    }

    fun deletePassword(pw: Password): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_COL, pw.pwID)
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
