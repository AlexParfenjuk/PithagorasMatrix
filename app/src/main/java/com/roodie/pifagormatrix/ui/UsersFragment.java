package com.roodie.pifagormatrix.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.adapters.UsersAdapter;
import com.roodie.pifagormatrix.model.User;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roodie on 15.03.2015.
 */
public class UsersFragment extends ListFragment {

    ListView listView;
    private ArrayList<User> allUsers;
    private static final String VERSION_UNAVAILABLE = "N/A";

    private UsersAdapter usersAdapter;
    private Button addUserButton;
    public static final String TAG = "UsersFragment";
    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getBaseContext();
        ActionBar actionBar = ((MainActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.all_users);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        PythagorasMatrixDatabase dbHelper = null;
        try {
            dbHelper = new PythagorasMatrixDatabase(getActivity().getApplicationContext());
            allUsers = dbHelper.getAllUsers();
           // for(User item : allUsers) {
             //   System.out.println(item.toString()); }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }


        usersAdapter = new UsersAdapter(context, allUsers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(usersAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(android.R.id.list);
        addUserButton = (Button) view.findViewById(R.id.add_user_button);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).showMatrix(allUsers.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).showEditUserFragment(allUsers.get(position));
                return true;
            }
        });

        this.addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showAddUserFragment();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.all_users);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AlexParfenjuk/PithagorasMatrix"));
                startActivity(browserIntent);
            }
                return true;
            case R.id.action_dark_theme: {
                Prefs.setDarkTheme(getActivity(), !item.isChecked());
                ((MainActivity) getActivity()).onChangeTheme();
            }
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAboutDialog() {
    }

    public String getVersionName() {
        String versionName;
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity()
                    .getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = VERSION_UNAVAILABLE;
        }
        return versionName;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_dark_theme).setChecked(Prefs.isDarkTheme(getActivity()));
    }
}
