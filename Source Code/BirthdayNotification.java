package markshannon.android.birthdayreminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by mshan on 2/6/2018.
 */

public class BirthdayNotification extends IntentService {

    private static final String TAG = "BirthdayNotification";
    private static final String TAGNAME = "NotificationName";
    private static final String TAGDATE = "BirthdayDate";
    private static final String EXTRA_NAME = "com.markshannon.android.birthdayreminder.NAME";
    private static final String EXTRA_ID = "com.markshannon.android.birthdayreminder.ID";

    // set interval to 1 minute for test
    //private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    private String name;
    private UUID id;

    public static Intent newIntent(Context context, Birthday birthday) {
        Intent i = new Intent(context, BirthdayNotification.class);
        i.setData(Uri.parse("id:"+birthday.getId()));
        return i;

    }

    public BirthdayNotification() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       // Log.i(TAG, "Received an intent: " + intent);


        name = (String) intent.getStringExtra(EXTRA_NAME);
        id = (UUID) intent.getSerializableExtra(EXTRA_ID);

       // Log.i(TAGNAME, "Name sent to notification: " + name);

        // send a notification when you get an intent from the alarm
        Resources resources = getResources();
        // intent to start the app
        Intent i = BirthdayListActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.notification_ticker))
                .setSmallIcon(R.drawable.ic_notify_birthday)
                .setContentTitle(resources.getString(R.string.notification_ticker))
                .setContentText(resources.getString(R.string.birthday_remind_text,name))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);

        // Update birthday: change to next year
        BirthdaySingleton birthdays = BirthdaySingleton.get(getApplicationContext());
        Birthday mBirthday = birthdays.getBirthday(id);
        setServiceAlarm(getApplicationContext(), false, mBirthday);
        mBirthday.updateBirthday();

        birthdays.updateBirthday(mBirthday);

        //Log.i(TAGDATE, "Updated date: " + mBirthday.displayDate());

        // cancel the birthday alarm and then set a new one for the next birthday

        setServiceAlarm(getApplicationContext(), true, mBirthday);
    }

    // sets an alarm to send a pending intent
    public static void setServiceAlarm(Context context, boolean isOn, Birthday birthday) {
        Intent i = BirthdayNotification.newIntent(context, birthday);
        i.putExtra(EXTRA_NAME, birthday.getName());
        i.putExtra(EXTRA_ID, birthday.getId());
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);



        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            //Log.i(TAGDATE, "Birthday date: " + birthday.getDate().toString());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, birthday.getDate().getTime(), pi);

        }
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
