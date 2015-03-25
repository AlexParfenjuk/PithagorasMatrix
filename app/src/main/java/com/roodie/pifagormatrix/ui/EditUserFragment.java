package com.roodie.pifagormatrix.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.model.User;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Roodie on 17.03.2015.
 */
public class EditUserFragment extends Fragment {

    public static final String TAG = "EditUserFragment";

    private Button editUserButton;
    private Button removeUserButton;
    private EditText usernameET;
    private EditText birthdayET;
    private ImageView userLogo;

    private Context context;
    private ActionBarActivity myContext;

    private PythagorasMatrixDatabase dbHelper;
    private User user = null;


    static void accessDB( final EditUserFragment editUserFragment, final PythagorasMatrixDatabase db){
        editUserFragment.dbHelper = db;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
        myContext = (ActionBarActivity)activity;
        ActionBar actionBar = ((MainActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.edit_user_fragment_title);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (User) bundle.getParcelable(User.USER_INSTANCE);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.userLogo = (ImageView) view.findViewById(R.id.user_logo);
        this.editUserButton = (Button) view.findViewById(R.id.edit_user_button);
        this.removeUserButton = (Button) view.findViewById(R.id.remove_user_button);
        this.usernameET = (EditText) view.findViewById(R.id.edit_username);
        this.birthdayET = (EditText) view.findViewById(R.id.edit_birthday);

        this.usernameET.setText(user.getUserName());
        updateUserBirthDate();

        this.birthdayET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserFragment.this.showDateDialog();
            }
        });

        this.usernameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    EditUserFragment.this.hideKeyboard();
                }
                return false;
            }
        });

        this.removeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context)EditUserFragment.this.getActivity())
                        .setIcon(R.drawable.ic_action_warning)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_access, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                try {
                                    EditUserFragment.accessDB(EditUserFragment.this, new PythagorasMatrixDatabase(context));
                                    EditUserFragment.this.dbHelper.deleteUser(user.getId());
                                    if (EditUserFragment.this.dbHelper != null) {
                                        EditUserFragment.this.dbHelper.close();
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                EditUserFragment.this.hideKeyboard();
                                ((MainActivity) EditUserFragment.this.getActivity()).showUsersList();
                            }
                        }).setNegativeButton(R.string.dialog_dismiss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        this.editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Operator operator = getListAdapter().getItem(getListView().getCheckedItemPosition() - 1);
                if (EditUserFragment.this.checkDataInput()) {
                    try {
                        EditUserFragment.accessDB(EditUserFragment.this, new PythagorasMatrixDatabase(context));
                        EditUserFragment.this.dbHelper.updateUser(user.getId(), usernameET.getText().toString(), user.getBirthday(), user.getMonth(), user.getYear());
                        if (EditUserFragment.this.dbHelper != null) {
                            EditUserFragment.this.dbHelper.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    EditUserFragment.this.hideKeyboard();
                    ((MainActivity) getActivity()).showUsersList();
                }
            }
        });
        this.usernameET.requestFocus();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_user_edit, container, false);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.edit_user_fragment_title);
        }
    }


    private void hideKeyboard() {
        final View currentFocus = this.getActivity().getCurrentFocus();
        if (currentFocus != null) {
            this.getActivity().getBaseContext();
            ((InputMethodManager)this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 2);
        }
    }


    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        c.set(user.getYear(),user.getMonth(),user.getBirthday());

        final DatePickerFragment datePickerFragment =DatePickerFragment.newInstance(c);
        datePickerFragment.setOnDialogResultListener(new DatePickerFragment.OnDialogResultListener() {
            @Override
            public void returnDate(Calendar calendar) {
                user.setBirthyear(calendar.get(Calendar.YEAR));
                user.setBirthmonth(calendar.get(Calendar.MONTH));
                user.setBirthday(calendar.get(Calendar.DAY_OF_MONTH));
                updateUserBirthDate();
            }
        });
        datePickerFragment.show(myContext.getFragmentManager(), "DATE_PICKER");

    }


    private boolean checkDataInput() {
        final String string = this.usernameET.getText().toString();
        if (!Utils.isOnlySymbols(string)) {
            Toast.makeText((Context) this.getActivity(), (CharSequence) this.getResources().getString(R.string.input_name_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (string.length() < 1) {
             this.usernameET.setHintTextColor(this.getResources().getColor(R.color.invalid));
             this.usernameET.requestFocus();
            return false;
        }
        if (this.birthdayET.getText().length() < 1) {
            this.showDateDialog();
            return false;
        }
        return true;
    }


    private void updateUserBirthDate() {
        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, user.getYear());
        instance.set(Calendar.MONTH, user.getMonth());
        instance.set(Calendar.DAY_OF_MONTH, user.getBirthday());
        this.birthdayET.setText((CharSequence)new SimpleDateFormat("dd MMMM yyyy", this.getResources().getConfiguration().locale).format(instance.getTime()));

    }


}
