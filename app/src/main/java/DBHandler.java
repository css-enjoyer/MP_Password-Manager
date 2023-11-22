import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "passwordDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "passwords";
    private static final String TITLE_COL = "pwTitle";
    private static final String ID_COL = "pwID";
    private static final String DESC_COL = "pwDesc";
    private static final String PW_COL = "password";



    // constructor for database handler
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // method for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create sqlite query, set col names and type
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT,"
                + DESC_COL + " TEXT,"
                + PW_COL + " TEXT)";
        // method to execute above sql query
        db.execSQL(query);
    }

    // method to add new pw to sqlite database
    public void addNewCourse(String pwTitle, String pwID, String pwDesc, String password) {

        // create variable for db and call writable to write
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // pass all values with kv pair
        values.put(TITLE_COL, pwTitle);
        values.put(ID_COL, pwID);
        values.put(DESC_COL, pwDesc);
        values.put(PW_COL, password);

        // pass values to table
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
