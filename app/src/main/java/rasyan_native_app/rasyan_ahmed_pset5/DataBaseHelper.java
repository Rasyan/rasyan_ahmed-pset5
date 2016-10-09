package rasyan_native_app.rasyan_ahmed_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Rasyan on 1-10-2016.
 *
 * This is the helper that regulates all interaction with the database,
 * it sets it up and contains the CRUD methods to modify it.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDB.db";
    private static int DATABASE_VERSION = 1;
    private String TABLE;

    private String text_id = "text";
    private String checked_id = "checked";

    // constructor that passes information to the super.
    // it also makes a new table if the table entered does not exists.
    public DataBaseHelper(Context context,String table) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE = table;
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + text_id + " TEXT, " + checked_id + " INTEGER)";
        db.execSQL(CREATE_TABLE);
        db.close();
    }

    // does nothing as all its functionality is in the constructor
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    // upgrades the database to a new version (not used in this app but required)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    // creates a new entry into the database,
    // the new text is added and its checked status is set to false.
    public void create(String input) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(text_id, input);
        values.put(checked_id, 0);
        db.insert(TABLE, null,values);
        db.close();
    }

    // reads the data from the database and puts it into a arraylist of TodoItems which is returned.
    public ArrayList<TodoItem> read() {
        ArrayList<TodoItem> data = new ArrayList<>();

        // gets the data from database and makes a cursor from it which is looped trough in a do while loop
        SQLiteDatabase db = getReadableDatabase();
        String query =  "SELECT _id , " + text_id + " , " + checked_id + " FROM " + TABLE;
        Cursor cursor = db.rawQuery(query, null);

        // loops trough the cursor and for each item in it it stores its values into the TodoItems,
        // that TodoItem is then added to the main data arraylist.
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(cursor.getColumnIndex(text_id)));
                item.setId(cursor.getLong(cursor.getColumnIndex("_id")));

                // SQLite has no booleans, this is why i store them as ints (0 or 1),
                // the line below converts the int into a boolean
                Boolean done = (cursor.getInt(cursor.getColumnIndex(checked_id)) == 1);
                item.setDone(done);
                data.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  data;
    }

    // updates the database item identified by a supplied id with
    // the new String and check status stored in ContentValues.
    public void update(long _id, String todo, boolean check) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(text_id, todo);

        // the line below converts the boolean into a int, so that it can be stored in the db.
        int flag = (check)? 1 : 0;
        contentValues.put(checked_id, flag);
        db.update(TABLE, contentValues, "_id = " + _id, null);
        db.close();
    }

    // delete the entry identified by the suplied id from the table.
    public void delete(long _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = " + _id,null);
        db.close();
    }


    // delete the whole table.
    public void deleteTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.close();
    }


}
