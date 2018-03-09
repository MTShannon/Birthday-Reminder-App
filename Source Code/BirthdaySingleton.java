package markshannon.android.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import markshannon.android.birthdayreminder.database.BirthdayBaseHelper;
import markshannon.android.birthdayreminder.database.BirthdayCursorWrapper;
import markshannon.android.birthdayreminder.database.BirthdayDbSchema;
import markshannon.android.birthdayreminder.database.BirthdayDbSchema.BirthdayTable;

/**
 * Created by mshan on 12/28/2017.
 */

//stores a list of the birthdays, exists as long as application is in memory
public class BirthdaySingleton {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static BirthdaySingleton sBirthdaySingleton;

    public static BirthdaySingleton get(Context context) {
        if (sBirthdaySingleton == null) {
            sBirthdaySingleton = new BirthdaySingleton(context);
        }
        return sBirthdaySingleton;
    }

    private BirthdaySingleton(Context context) {
        mContext = context.getApplicationContext();
        //When you call .getWritableDatabase(), CrimeBaseHelper creates a new database file if it DNE
        // it it's the first time the DB has been created calls onCreate(SQLite...), or calls
        // onUpgrade(SQLite... , int, int) depending on the version umbber
        mDatabase = new BirthdayBaseHelper(mContext).getWritableDatabase();

    }

    // adds a row to the database with a new birthday
    public void addBirthday(Birthday b) {

        ContentValues values = getContentValues(b);

        // first arg- table you want to insert to, last arg- data you want to put in
        mDatabase.insert(BirthdayTable.NAME, null, values);
    }

    // delete a birthday from the database
    public void deleteBirthday(Birthday b) {
        mDatabase.delete(BirthdayTable.NAME,
                BirthdayTable.Cols.UUID + " = ?",
                new String[] { b.getId().toString()});
    }

    public List<Birthday> getBirthdays() {

        List<Birthday> birthdays = new ArrayList<>();

        // (null, null) gets all rows?
        BirthdayCursorWrapper cursor = queryBirthdays(null, null);

        // go through the cursor, get a birthday, add it to the ArrayList, move cursor to next row data
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                birthdays.add(cursor.getBirthday());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return birthdays;
    }

    // returns a specific birthday
    public Birthday getBirthday(UUID id) {

        // search the database with a query for the id you're looking for
        BirthdayCursorWrapper cursor = queryBirthdays(
                BirthdayTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );


        try {
            // birthday with this id DNE
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getBirthday();
        } finally {
            cursor.close();
        }

    }

    // update rows in the database when something is changed
    public void updateBirthday(Birthday birthday) {
        String uuidString = birthday.getId().toString();
        ContentValues values = getContentValues(birthday);

        // update(String, ContentValues, String, String[])
        // 1st arg (String) - table name you want to update
        // 2nd arg (ContentValues) - ContentValues you want to assign to each row you update
        // 3rd arg (String) - specify which row to update with a where clause
        // 4th arg (String[]) - specify values for the arguments in the where clause
        mDatabase.update(BirthdayTable.NAME, values,
                BirthdayTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    //read in data from SQLite
    private BirthdayCursorWrapper queryBirthdays(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BirthdayTable.NAME, // table to read from
                null, // columns - null selects all columns
                whereClause, // whereClause & args specify which row (UUID?)
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new BirthdayCursorWrapper(cursor);
    }

    // takes care of putting a Birthday into a ContentValues
    // ContentValues is a key-value store class, stores data
    private static ContentValues getContentValues(Birthday birthday) {
        // use column names as keys, _id is automatically created as a unique row ID
        ContentValues values = new ContentValues();
        values.put(BirthdayTable.Cols.UUID, birthday.getId().toString());
        values.put(BirthdayTable.Cols.PERSON, birthday.getName());
        values.put(BirthdayTable.Cols.DATE, birthday.getDate().getTime());

        return values;
    }
}



