package markshannon.android.birthdayreminder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import markshannon.android.birthdayreminder.Birthday;
import markshannon.android.birthdayreminder.database.BirthdayDbSchema.BirthdayTable;

/**
 * Created by mshan on 1/22/2018.
 */

// CursorWrapper lets you wrap a Cursor you received from another place
// and add new methods on top of it
public class BirthdayCursorWrapper extends CursorWrapper {

    public BirthdayCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // pulls out relevant column data
    public Birthday getBirthday() {
        String uuidString = getString(getColumnIndex(BirthdayTable.Cols.UUID));
        String personName = getString(getColumnIndex(BirthdayTable.Cols.PERSON));
        long date = getLong(getColumnIndex(BirthdayTable.Cols.DATE));

        Birthday birthday = new Birthday(UUID.fromString(uuidString));
        birthday.setName(personName);
        birthday.setDate(new Date(date));

        return birthday;
    }
}
