package markshannon.android.birthdayreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;


/**
 * Created by mshan on 12/28/2017.
 */

// screen where you edit the name and date of a birthday entry
// pulls up the detail page
public class BirthdayActivity extends SingleFragmentActivity{

    public static final String EXTRA_BIRHTDAY_ID = "mark.shannon.android.birthdayreminder.birthday_id";

    public static Intent newIntent(Context packageContext, UUID birthdayId) {
        Intent intent = new Intent(packageContext, BirthdayActivity.class);
        intent.putExtra(EXTRA_BIRHTDAY_ID, birthdayId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID birthdayId = (UUID) getIntent().getSerializableExtra(EXTRA_BIRHTDAY_ID);
        return BirthdayFragment.newInstance(birthdayId);
    }
}
