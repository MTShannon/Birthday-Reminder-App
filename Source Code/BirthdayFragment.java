package markshannon.android.birthdayreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mshan on 12/28/2017.
 */

// Birthday Fragment is what pops up when someone wants to edit a birthday
// The detail page
public class BirthdayFragment extends Fragment{

    private static final String ARG_BIRTHDAY_ID = "birthday_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final String TAG = "DateReceived";


    private static final int REQUEST_DATE = 0;

    private EditText mNameText;
    private Button mDateButton;
    private Birthday mBirthday;

    public BirthdayFragment() {

    }

    public static BirthdayFragment newInstance(UUID birthdayId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BIRTHDAY_ID, birthdayId);

        BirthdayFragment fragment = new  BirthdayFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID birthdayId = (UUID) getArguments().getSerializable(ARG_BIRTHDAY_ID);
        mBirthday = BirthdaySingleton.get(getActivity()).getBirthday(birthdayId);

        setHasOptionsMenu(true);
    }

    // Birthdays get modified in BirthdayFragment and need to be updated when BirthdayFragment is done
    @Override
    public void onPause() {
        super.onPause();

        BirthdaySingleton.get(getActivity()).updateBirthday(mBirthday);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_birthday, container, false);

        mNameText = (EditText) v.findViewById(R.id.name);
        mNameText.setText(mBirthday.getName());
        mNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {

                // left blank on purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBirthday.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // left blank on purpose
            }
        });

        mDateButton = (Button) v.findViewById(R.id.date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBirthday.getDate());

                // BirthdayFragment is target fragment of DatePickerFragment
                // target fragment can use requestCode to later see which fragment is reporting back
                dialog.setTargetFragment(BirthdayFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        return v;
    }

    //inflates the delete menu action item view
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // puts the menu view up in the menu instance
        inflater.inflate(R.menu.fragment_birthday_delete, menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_birthday:
                UUID birthdayId = (UUID) getArguments().getSerializable(ARG_BIRTHDAY_ID);
                BirthdaySingleton birthdays = BirthdaySingleton.get(getActivity());
                mBirthday = birthdays.getBirthday(birthdayId);

                // cancel alarm when you delete a birthday
                BirthdayNotification.setServiceAlarm(getActivity(), false, mBirthday);

                birthdays.deleteBirthday(mBirthday);
                getActivity().finish();
                return true;

                // return true to indicate no further processing is needed

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // checks if something was entered into the date, added as an extra to the intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Log.i(TAG, "Year Received from datePicker: " + date.getYear());
            mBirthday.setDate(date);
            updateDate();

            // when user picks a date, set an alarm to go off at that date
            BirthdayNotification.setServiceAlarm(getActivity(), false, mBirthday);
            BirthdayNotification.setServiceAlarm(getActivity(), true, mBirthday);
        }
    }

    private void updateDate() {
        mDateButton.setText(mBirthday.displayDate());
    }
}

