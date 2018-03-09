package markshannon.android.birthdayreminder;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mshan on 12/28/2017.
 */

public class Birthday {

    public Birthday() {
        this(UUID.randomUUID());
    }

    // constructor used when reading birthday from database
    public Birthday(UUID id) {
        mId = id;
        mDate = new Date();
    }

    private Date mDate;
    private String name;
    private UUID mId;

    private final static String TAG = "DateDisplayed";

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        // see if the birthday has already happened this year
        Date today = new Date();


        if (dateInPast(date, today)) {
            date.setYear(today.getYear() + 1);
        }

        // set time of date to 9:00 am for notification purposes
        date.setHours(9);
        date.setMinutes(00);
        date.setSeconds(0);

        mDate = date;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String displayDate() {
        String dateString;
        String month = "";
        switch (mDate.getMonth()) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;

        }

        dateString = month + " " + mDate.getDate() + ", " + (mDate.getYear() + 1900);

        return dateString;
    }

    // check if date entered is in the past
    private static Boolean dateInPast(Date testDate, Date today) {
        if (testDate.getYear() < today.getYear()) {

            return true;
        } else if (testDate.getMonth() < today.getMonth() && testDate.getYear() == today.getYear()) {
            return true;
        } else if (testDate.getDate() < today.getDate() && testDate.getYear() == today.getYear()
                && testDate.getMonth() == today.getMonth()) {

            return true;
        }

        return false;

    }


    public void updateBirthday() {
        mDate.setYear(mDate.getYear() + 1);
    }

}
