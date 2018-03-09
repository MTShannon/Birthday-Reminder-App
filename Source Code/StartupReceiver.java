package markshannon.android.birthdayreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by mshan on 2/22/2018.
 */

// class resets all the alarms after the phone fully turns off
public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i(TAG, "Received broadcast intent: " + intent.getAction());

        BirthdaySingleton birthdays = BirthdaySingleton.get(context);
        List<Birthday> listBirthdays = birthdays.getBirthdays();

        for(Birthday b : listBirthdays) {
            BirthdayNotification.setServiceAlarm(context, true, b);
        }
    }
}
