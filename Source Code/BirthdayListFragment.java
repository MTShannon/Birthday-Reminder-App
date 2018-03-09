package markshannon.android.birthdayreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mshan on 12/28/2017.
 */

// list fragment that shows up first
public class BirthdayListFragment extends Fragment {

    private Birthday mBirthday;
    private TextView mNameField;
    private TextView mDateField;

    private RecyclerView mBirthdayRecyclerView;
    private ConstraintLayout mBirthdayConstraintLayout;
    private ImageButton mImageButton;
    private BirthdayAdapter mAdapter;


    private static final String OnResume = "OnResume";

    private static final String TAGRESUME = "OnResume";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // let the FragmentManager know that the fragment should receive a call to onCreateOptionsMenu(...)
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        final BirthdaySingleton mBirthdaySingleton = BirthdaySingleton.get(getActivity());
        List<Birthday> mBirthdays = mBirthdaySingleton.getBirthdays();

        // inflate the fragments view with layout resource id (where the view goes), the container
        // (the view's parent), and 3rd parameter, tells layout inflater whether to add the inflated view
        // to the view's parent, false because add view in activit's code
        view = inflater.inflate(R.layout.fragment_birthday_list, container, false);

        mBirthdayRecyclerView = (RecyclerView) view.findViewById(R.id.birthday_recycler_view);
        mBirthdayRecyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

        mBirthdayConstraintLayout = (ConstraintLayout) view.findViewById(R.id.empty_list_display);
        mImageButton = (ImageButton) view.findViewById(R.id.first_add_button);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // puts the menu view up in the menu instance
        // if there are no birthdays, don't show the menu, just show the view with the + button
        BirthdaySingleton mBirthdaySingleton = BirthdaySingleton.get(getActivity());
        List<Birthday> mBirthdays = mBirthdaySingleton.getBirthdays();

        if (!mBirthdays.isEmpty()) {
            inflater.inflate(R.menu.fragment_birthday_list, menu);
        }
    }

    // when an action item is pressed, onOptionsItemSelected(...) is called
    // the MenuItem argument describes what was done
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_birthday:
                Birthday birthday = new Birthday();
                BirthdaySingleton.get(getActivity()).addBirthday(birthday);
                Intent intent = BirthdayActivity.newIntent(getActivity(), birthday.getId());
                startActivity(intent);

                // return true to indicate no further processing is needed
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // instantiate the adapter by passing in the list of birthdays from the singleton
    // connect the RecyclerView with the Adapter
    private void updateUI() {
        final BirthdaySingleton singleton = BirthdaySingleton.get(getActivity());
        List<Birthday> birthdays = singleton.getBirthdays();

        if(mAdapter == null) {
            mAdapter = new BirthdayAdapter(birthdays);
            mBirthdayRecyclerView.setAdapter(mAdapter);
        }
        else {
            // have to update adapter because the List<Birthdays> returned by getBirthdays() is
            // like a snapshot of the Birthday(s) at one point in time
            mAdapter.setBirthdays(birthdays);
            mAdapter.notifyDataSetChanged();
        }

        if (birthdays.isEmpty()) {
            mBirthdayConstraintLayout.setVisibility(View.VISIBLE);

            mImageButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                       public void onClick(View v) {
                            Birthday birthday = new Birthday();
                            singleton.addBirthday(birthday);
                            Intent intent = BirthdayActivity.newIntent(getActivity(), birthday.getId());
                           startActivity(intent);
                        }
                   });
        }
        else {
            mBirthdayConstraintLayout.setVisibility(View.GONE);
        }
    }

    // Adapter that binds a view holder and it's data
    public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayViewHolder> {
        private List<Birthday> mBirthdays;

        public BirthdayAdapter(List<Birthday> birthdays) {
            mBirthdays = birthdays;
        }

        // creates and returns a view holder when RecyclerView needs a  new ViewHolder to display
        // create a LayoutInflater and use it for viewholder
        @Override
        public BirthdayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BirthdayViewHolder(layoutInflater, parent);
        }

        // binds the birthday data to the view holder
        @Override
        public void onBindViewHolder(BirthdayViewHolder holder, int position) {
            Birthday birthday = mBirthdays.get(position);
            holder.bind(birthday);
        }

        // returns # of birthdays in the list
        @Override
        public int getItemCount() {
            return mBirthdays.size();
        }


        public void setBirthdays(List<Birthday> birthdays) {
            mBirthdays = birthdays;
        }
    }

    // View holder that holds a View for a birthday item
    private class BirthdayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mDateTextView;
        private Birthday mbirthday;

        public BirthdayViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.birthday_item, parent, false));

            mNameTextView = (TextView) itemView.findViewById(R.id.birthday_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.birthday_date);

            itemView.setOnClickListener(this);
        }

        public void bind(Birthday birthday) {
            mbirthday = birthday;
            mNameTextView.setText(mbirthday.getName());
            mDateTextView.setText(mbirthday.displayDate());
        }

        @Override
        public void onClick(View v) {
            Intent intent = BirthdayActivity.newIntent(getActivity(), mbirthday.getId());
            startActivity(intent);
        }


    }



}
