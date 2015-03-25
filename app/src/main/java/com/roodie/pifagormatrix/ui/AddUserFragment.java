package com.roodie.pifagormatrix.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Roodie on 15.03.2015.
 */
public class AddUserFragment extends Fragment {

    public static final String TAG = "AddUserFragment";

    private Button addUserButton;
    private EditText dateEditText;
    private EditText userNameEditText;
    private ImageView userLogo;

    private PythagorasMatrixDatabase dbHelper;
    Context context;

    private ActionBarActivity myContext;

    private int userBirthDay;
    private int userBirthMonth;
    private int userBirthYear;

    static void accessDB (final AddUserFragment addUserFragment, final PythagorasMatrixDatabase db) {
        addUserFragment.dbHelper = db;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
        myContext = (ActionBarActivity) activity;
        ActionBar actionBar = ((MainActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.add_user);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.userLogo = (ImageView) view.findViewById(R.id.add_user_image);
        this.addUserButton = (Button) view.findViewById(R.id.add_user_button);
        this.dateEditText = (EditText) view.findViewById(R.id.add_birthday);
        this.userNameEditText = (EditText) view.findViewById(R.id.add_username);

        this.dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserFragment.this.showDateDialog();
            }
        });


        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Operator operator = getListAdapter().getItem(getListView().getCheckedItemPosition() - 1);
                if (AddUserFragment.this.checkDataInput()) {
                    try {
                        AddUserFragment.accessDB(AddUserFragment.this, new PythagorasMatrixDatabase(context));
                        AddUserFragment.this.dbHelper.insertUser(userNameEditText.getText().toString(), AddUserFragment.this.userBirthDay, AddUserFragment.this.userBirthMonth, AddUserFragment.this.userBirthYear);
                        if (AddUserFragment.this.dbHelper != null) {
                            AddUserFragment.this.dbHelper.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    AddUserFragment.this.hideKeyboard();
                    ((MainActivity) getActivity()).showUsersList();
                }
            }
        });
        this.userNameEditText.requestFocus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_user_add, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.add_user);
        }
    }


    private void hideKeyboard() {
        final View currentFocus = this.getActivity().getCurrentFocus();
        if (currentFocus != null) {
            this.getActivity().getBaseContext();
            ((InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 2);
        }
    }


    private boolean checkDataInput() {
        final String string = this.userNameEditText.getText().toString();
        if (!Utils.isOnlySymbols(string)) {
            Toast.makeText((Context) this.getActivity(), (CharSequence) this.getResources().getString(R.string.input_name_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (string.length() < 1) {
            this.userNameEditText.setHintTextColor(this.getResources().getColor(R.color.invalid));
            this.userNameEditText.requestFocus();
            return false;
        }
        if (this.dateEditText.getText().length() < 1) {
            this.showDateDialog();
            return false;
        }
        return true;
    }


    private void updateUserBirthDate() {
        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, this.userBirthYear);
        instance.set(Calendar.MONTH, this.userBirthMonth);
        instance.set(Calendar.DAY_OF_MONTH, this.userBirthDay);

        this.dateEditText.setText((CharSequence) new SimpleDateFormat("dd MMMM yyyy", this.getResources().getConfiguration().locale).format(instance.getTime()));

    }

    private void showDateDialog() {

        final DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDialogResultListener(new DatePickerFragment.OnDialogResultListener() {
            @Override
            public void returnDate(Calendar calendar) {
                userBirthYear = calendar.get(Calendar.YEAR);
                userBirthMonth = calendar.get(Calendar.MONTH);
                userBirthDay = calendar.get(Calendar.DAY_OF_MONTH);
                updateUserBirthDate();
            }
        });
        datePickerFragment.show(myContext.getFragmentManager(), "DATE_PICKER");

    }

}
