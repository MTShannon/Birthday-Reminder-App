package markshannon.android.birthdayreminder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// class that hosts the list fragment that shows all the birthdays
public class BirthdayListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BirthdayListFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, BirthdayListActivity.class);
    }
}
