package markshannon.android.birthdayreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import markshannon.android.birthdayreminder.database.BirthdayDbSchema.BirthdayTable;

/**
 * Created by mshan on 1/22/2018.
 */

// class helps with opening the database file by handing if it alread exists, creating it, check the
// version, upgrade version, etc.
public class BirthdayBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "birthdayBase.db";

    public BirthdayBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table definition code
        db.execSQL("create table " + BirthdayTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            BirthdayTable.Cols.UUID + ", " +
            BirthdayTable.Cols.PERSON + ", " +
            BirthdayTable.Cols.DATE +
            ")"
        );
    }

    // can ignore for now because BirthdayReminder will have just 1 version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
