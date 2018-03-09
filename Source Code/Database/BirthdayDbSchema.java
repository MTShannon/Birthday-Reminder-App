package markshannon.android.birthdayreminder.database;

/**
 * Created by mshan on 1/22/2018.
 */

public class BirthdayDbSchema {

    // exists to define string constants needed to describe pieces of the table definition
    public static final class BirthdayTable {
        public static final String NAME = "birthdays";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PERSON = "title";
            public static final String DATE = "date";
        }
    }
}
